package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import common.CallbackProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Handler implementation for the TCP client.  It initiates the ping-pong
 * traffic between the TCP client and server by sending the first message to
 * the server.
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<Object > {

    static int byteRead1 = 0,SerialDatalength=0;
    static int  start2=0;
     int[] Rev_IVsystemlitle = new int[12];
    OndemandParameters cmd = new OndemandParameters();
    int[] messagebuffer = null;
	String message;
	CallbackProcessor asynchCall;
	boolean close = false;

    public TcpClientHandler(String message, CallbackProcessor asynchCall) {
    	this.message = message;
    	this.asynchCall = asynchCall;
    }

    public TcpClientHandler(OndemandParameters cmd,int[] messagebuffer, CallbackProcessor asynchCall)
    {
        this.cmd=cmd;
      //  this.message = message;
        this.messagebuffer = messagebuffer;
        this.asynchCall = asynchCall;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
    	//ctx.writeAndFlush(this.message);
          cmd.RequestFramecounter=0;
           DLMS.AARQFrame(GlobalDeclarations.arrq, GlobalDeclarations.hexStringTointArray(cmd.EKEY),GlobalDeclarations.hexStringTointArray(cmd.AKEY),GlobalDeclarations.RQ_IVsystemlitle,   cmd.RequestFramecounter,1,ctx);
    }


    @Override
    public void channelRegistered(@NotNull ChannelHandlerContext ctx)  {
    	System.out.println("channelRegistered "+ ctx.channel());
     }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {    	
    	System.out.println("channelUnregistered "+ ctx.channel() + cmd.MeterIP);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {    	
    	System.out.println("channelInactive "+ctx.channel() + "    " +cmd.MeterIP);
    }   

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	 ctx.flush();
         
         //close the connection after flushing data to client
    	 if(close){
    		 ctx.close();
             long totalsec = cmd.end-cmd.start;
             System.out.println("Total sec is : " + totalsec);
    	 }         
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.asynchCall.process(msg);
      //  System.out.print("Meter:" + cmd.MeterIP + ":"+cmd.MeterPort);
        ByteBuf inBuffer = (ByteBuf) msg;
        String received = inBuffer.toString(CharsetUtil.US_ASCII);
        String hexaString = ByteBufUtil.hexDump(inBuffer);
       int[] SerialDatarev = GlobalDeclarations.hexStringTointArray(hexaString);
       // GlobalDeclarations.dataprint("\n Response PlayLoad :", SerialDatarev,SerialDatarev.length);
        if (SerialDatarev[0] == 0x00)
        {
            if (SerialDatarev.length > 8)
            {
                SerialDatalength = ((SerialDatarev[6] << 8) | SerialDatarev[7]);
                //	  System.out.println("Length of packet : "+byteRead1 + "\n");
                if ((SerialDatalength + 8) == SerialDatarev.length)
                {
                    switch (SerialDatarev[8])
                    {
                        case (int) GlobalDeclarations.TAG.TAG_AARE:
                        {

                        //   GlobalDeclarations.dataprint("\nAARQ Response:", SerialDatarev,SerialDatalength);
                            System.out.print("Meter:" + cmd.MeterIP + ":"+cmd.MeterPort+ "-->>  AARQ Response");
                            if(SerialDatarev[21]==0xA2 &&  SerialDatarev[25]==0x01)
                            {
                                        DLMS.RLRQ_Frame(GlobalDeclarations.hexStringTointArray(cmd.EKEY),GlobalDeclarations.hexStringTointArray(cmd.AKEY),cmd.RequestFramecounter,1,ctx);

                            }
                            else
                            {
                                int[] S2cKey = new int[16];
                                System.arraycopy(SerialDatarev, 37, Rev_IVsystemlitle, 0, 8);
                                System.arraycopy(SerialDatarev, 62, S2cKey, 0, SerialDatarev[61]);
                                System.arraycopy(SerialDatarev, 85, Rev_IVsystemlitle, 8, 4);
                                AESGCM.aes_encrypt(S2cKey, GlobalDeclarations.HLSkey);
                                //    GlobalDeclarations.dataprint("S2cKey", S2cKey, S2cKey.length);
                                cmd.RequestFramecounter++;

                                 DLMS.AAREFrame(S2cKey, GlobalDeclarations.RQ_IVsystemlitle, GlobalDeclarations.USOBIS, GlobalDeclarations.USIC, GlobalDeclarations.USATTB, cmd.RequestFramecounter, 1,ctx);


                            }
                            break;
                        }

                        case (int) GlobalDeclarations.TAG.ACTION_RES_GLO:
                        {
                            // tag comparision and command checking
                         //  GlobalDeclarations.dataprint("AARE Response", SerialDatarev, SerialDatarev.length);
                            System.out.print("Meter:" + cmd.MeterIP + ":"+cmd.MeterPort+ "-->>  AARE Response");
                            int length = SerialDatarev[9];
                            System.arraycopy(SerialDatarev, 11, Rev_IVsystemlitle, 8, 4);
                           //  GlobalDeclarations.dataprint("Rev_IVsystemlitle", Rev_IVsystemlitle, Rev_IVsystemlitle.length);
                            int[] plaintext = new int[length - 5 - 12];
                            int[] chipertext = new int[plaintext.length];
                            System.arraycopy(SerialDatarev, 15, plaintext, 0, length - 17);
                            //   GlobalDeclarations.dataprint("\nplaintext", plaintext, plaintext.length);

                            AESGCM.Aes_Gcm_Decrypt_Encrypt(length - 17, GlobalDeclarations.Enkey, Rev_IVsystemlitle, plaintext, 17, GlobalDeclarations.Authkey, chipertext, GlobalDeclarations.DETAG, 0);

                             //  GlobalDeclarations.dataprint("\nchipertext", chipertext, chipertext.length);
                            //   GlobalDeclarations.dataprint("\nDETAG", GlobalDeclarations.DETAG, GlobalDeclarations.DETAG.length);

                            cmd.RequestFramecounter++;
                             if(cmd.msg_type.equals("GET"))
                            DLMS.APDUFRAME_GET(GlobalDeclarations.hexStringTointArray("C8"), GlobalDeclarations.hexStringTointArray("C001C1"), GlobalDeclarations.RQ_IVsystemlitle, GlobalDeclarations.hexStringTointArray(cmd.Obis), GlobalDeclarations.hexStringTointArray(cmd.Ic),GlobalDeclarations.hexStringTointArray(cmd.Attributes), cmd.RequestFramecounter, 1,ctx);
                            else if(cmd.msg_type.equals("ACTION")) {
                                 DLMS.APDUEXECUTE(GlobalDeclarations.hexStringTointArray("CB"), GlobalDeclarations.hexStringTointArray("C301C1"), GlobalDeclarations.RQ_IVsystemlitle, GlobalDeclarations.hexStringTointArray(cmd.Obis), GlobalDeclarations.hexStringTointArray(cmd.Ic), GlobalDeclarations.hexStringTointArray(cmd.Attributes), GlobalDeclarations.hexStringTointArray("0F00"), cmd.RequestFramecounter, 1, ctx);
                                    cmd.msg_type="ACTION1";
                            }
                            else if(cmd.msg_type.equals("ACTION1")) {
                                 GlobalDeclarations.dataprint( cmd.MeterIP+ "ACTION Decrypt Response", chipertext,chipertext.length);
                                 DLMS.RLRQ_Frame(GlobalDeclarations.hexStringTointArray(cmd.EKEY),GlobalDeclarations.hexStringTointArray(cmd.AKEY),cmd.RequestFramecounter,1,ctx);
                             }

                        }
                        break;

                        case (int) GlobalDeclarations.TAG.GET_RES_GLO:
                            //GlobalDeclarations.dataprint("\n\nAPDU Response", SerialDatarev, SerialDatarev.length);
                            System.out.print("Meter:" + cmd.MeterIP + ":"+cmd.MeterPort+ "-->>  APDU Response \n");
                            int Packetlength = 0;
                            int skplength = 0;
                            if (SerialDatarev[9] < 0x80)
                            {
                                //  GlobalDeclarations.dataprint("\nPacket length is Less than 256bytes", SerialDatarev, 0);
                                Packetlength = SerialDatarev[9];
                                System.arraycopy(SerialDatarev, 11, Rev_IVsystemlitle, 8, 4);
                                Packetlength = Packetlength - ((int) GlobalDeclarations.llength.AUTHTAG + (int) GlobalDeclarations.llength.SECURITY + (int) GlobalDeclarations.llength.FRAME_COUNT_LEN);
                                skplength = 10 + ((int) GlobalDeclarations.llength.SECURITY + (int) GlobalDeclarations.llength.FRAME_COUNT_LEN);

                            }
                            else if (SerialDatarev[9] == 0x81)
                            {
                                //  GlobalDeclarations.dataprint("\nNumber of Packet length is greater than 128bytes\n", SerialDatarev, 0);
                                Packetlength = SerialDatarev[10];
                                System.arraycopy(SerialDatarev, 12, Rev_IVsystemlitle, 8, 4);
                                Packetlength = Packetlength - ((int) GlobalDeclarations.llength.AUTHTAG + (int) GlobalDeclarations.llength.SECURITY + (int) GlobalDeclarations.llength.FRAME_COUNT_LEN);
                                skplength = 11 + ((int) GlobalDeclarations.llength.SECURITY + (int) GlobalDeclarations.llength.FRAME_COUNT_LEN);

                            }
                            else if (SerialDatarev[9] == 0x82)
                            {
                                // GlobalDeclarations.dataprint("\nPacket length is greater than 256bytes\n", SerialDatarev, 0);
                                Packetlength = (SerialDatarev[10] << 8) | SerialDatarev[11];
                                System.arraycopy(SerialDatarev, 13, Rev_IVsystemlitle, 8, 4);
                                Packetlength = Packetlength - ((int) GlobalDeclarations.llength.AUTHTAG + (int) GlobalDeclarations.llength.SECURITY + (int) GlobalDeclarations.llength.FRAME_COUNT_LEN);
                                skplength = 12 + ((int) GlobalDeclarations.llength.SECURITY + (int) GlobalDeclarations.llength.FRAME_COUNT_LEN);
                            }

                            int[] Revchipertext = new int[Packetlength];
                            int[] Revplaintext = new int[Revchipertext.length];
                            System.arraycopy(SerialDatarev, skplength, Revchipertext, 0, Packetlength);
                          //  GlobalDeclarations.dataprint( cmd.MeterIP+ "   Systemlitle Key", Rev_IVsystemlitle,Rev_IVsystemlitle.length);
                            AESGCM.Aes_Gcm_Decrypt_Encrypt(Revchipertext.length, GlobalDeclarations.Enkey, Rev_IVsystemlitle, Revchipertext, 17, GlobalDeclarations.Authkey, Revplaintext, GlobalDeclarations.DETAG, 0);
                            GlobalDeclarations.dataprint( cmd.MeterIP+ "--->> Decrypt Response", Revplaintext,Revplaintext.length);

                            if(Revplaintext[1]==0x01) {  //TAG_GET_RES_NORMAL
                                cmd.RequestFramecounter++;
                                DLMS.RLRQ_Frame(GlobalDeclarations.hexStringTointArray(cmd.EKEY),GlobalDeclarations.hexStringTointArray(cmd.AKEY),cmd.RequestFramecounter, 1, ctx);
                            }
                            else   if(Revplaintext[1]==0x02) {//TAG_GET_RES_BLOCK
                                if(Revplaintext[9]<0x80)   // number packets are there in meter
                                {
                                    System.out.print("\nNumber of Packet length is less than 128bytes\n");
                                    cmd.Packetscount=Revplaintext[11];
                                    System.out.print("Number of packets available is "+ cmd.Packetscount);

                                }
                                else if(Revplaintext[9]==0x81) // number packets are there in meter
                                {
                                    System.out.print("\nNumber of Packet length is greater than 128bytes\n");
                                }
                                else if(Revplaintext[9]==0x82) // number packets are there in meter
                                {
                                    System.out.print("\nnumber of Packet length is greater than 256bytes\n");
                                }
                            }

                                break;

                        case (int) GlobalDeclarations.TAG.RLRE:
                            //GlobalDeclarations.dataprint("\nRLRE DATA", SerialDatarev,SerialDatarev.length);
                            System.out.print("Meter:" + cmd.MeterIP + ":"+cmd.MeterPort+ "-->>  RLRE Response");
                            close = true;
                            cmd.end = System.currentTimeMillis();
                            break;

                    }
                }
            }
        }

      /*  if("close".equals(msg)){
            close = true;
        }*/

    }
}