package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DLMS {
    public static int [] AARQFrame(int[] AARQ,int[] EKEY,int[]ADD,int[] systemtitle, long framecounter,int Method, ChannelHandlerContext ctx)
    {
				   int[] arrqplaintext = { 0x01, 0x00, 0x00, 0x00, 0x06, 0x5F, 0x1F, 0x04, 0x00, 0x00, 0x18, 0x1D, 0xFF, 0xFF };
				
				   int[] Bulidframe = new int[AARQ.length+systemtitle.length+ arrqplaintext.length+4];
				   int[] chipertext = new int[arrqplaintext.length+2];
				   int[] auth_tag = new int[12];
				   
				   int [] test= new int[2];
				    System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
				    AESGCM.Aes_Gcm_Decrypt_Encrypt(14,EKEY, systemtitle, arrqplaintext,17, ADD, chipertext,auth_tag, Method);
				
				    System.arraycopy(AARQ, 0, Bulidframe, 0, AARQ.length);
				   System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, Bulidframe, AARQ.length, 4);
				    System.arraycopy(chipertext, 0, Bulidframe, AARQ.length+4, arrqplaintext.length);
				   System.arraycopy(auth_tag, 0, Bulidframe, AARQ.length + 4+ arrqplaintext.length, 12);
        ByteBuf byteBuf = Unpooled.buffer(Bulidframe.length);
        for(int i=0;i<Bulidframe.length;i++)
        {
            byteBuf.writeByte((byte)Bulidframe[i]);
        }
        ctx.writeAndFlush(byteBuf);
        return Bulidframe;
    }
    
    
    public static int[] RLRQ_Frame(int[] EKEY,int[]ADD,long framecounter, int Method,ChannelHandlerContext ctx)
    {

        int[] RLRQplaintext = { 0x01, 0x00, 0x00, 0x00, 0x06, 0x5F, 0x1F, 0x04, 0x00, 0x00, 0x18, 0x1D, 0xFF, 0xFF };
        int[] RRLQ = { 0x00, 0x01, 0x00, 0x30, 0x00, 0x01, 0x00, 0x2A, 0x62, 0x28, 0x80, 0x01, 0x00, 0xBE, 0x23, 0x04, 0x21, 0x21, 0x1F, 0x30, 0x00, 0x00, 0x01, 0x5D, 0x01, 0x00, 0x00, 0x00, 0x06, 0x5F, 0x1F, 0x04, 0x00, 0x00, 0x18, 0x1D, 0xFF, 0xFF, 0xE0, 0x7D, 0x0B, 0x83, 0xEB, 0x07, 0xA7, 0xEF, 0x5C, 0xFA, 0xF2, 0x99 };

    //    int[] Bulidframe = new int[50];
        int[] chipertext = new int[RLRQplaintext.length];
        int[] auth_tag = new int[12];
        //     void Aes_Gcm_Decrypt_Encrypt(int DataLength, byte[] Key, byte[] InitVector, byte[] PlainText, int AAD_Length, byte[] AAD, byte[] CipherText, byte[] Tag, int Method)
        System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, GlobalDeclarations.RQ_IVsystemlitle, 8, 4);
        AESGCM.Aes_Gcm_Decrypt_Encrypt(14, GlobalDeclarations.Enkey, GlobalDeclarations.RQ_IVsystemlitle, RLRQplaintext, 17, GlobalDeclarations.Authkey, chipertext, auth_tag, Method);

        System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, RRLQ, 20, 4);

        System.arraycopy(chipertext, 0, RRLQ, 24, RLRQplaintext.length);
        System.arraycopy(auth_tag, 0, RRLQ, RLRQplaintext.length + 24, 12);
        ByteBuf byteBuf = Unpooled.buffer(RRLQ.length);
        for(int i=0;i<RRLQ.length;i++)
        {
            byteBuf.writeByte((byte)RRLQ[i]);
        }
        ctx.writeAndFlush(byteBuf);
        return RRLQ;
    }
    
    
    public static int[] AAREFrame(int[] S2c, int[] systemtitle, int[] OBIS, int[] IC, int[] Attribute, long framecounter, int Method, ChannelHandlerContext ctx)
    {
        int[] wrapper = new int[58];
        int length = 6;
        int[] starting_frame = {0x00,0x01,0x00,0x30,0x00,0x01};
        
        int[] chipertext = new int [31];
        int[] auth_tag = new int[12];

        System.arraycopy(starting_frame, 0, wrapper, 0, length);
        wrapper[length++] = 0x00;
        wrapper[length++] = 0x32; //length
        wrapper[length++] = GlobalDeclarations.TAG.ACTION_REQ_GLO;
        wrapper[length++] = 0x30; // length 
        wrapper[length++] = 0x30;
      //  length = length + 5;
        System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, wrapper, length, 4);
        length = length + 4;
        wrapper[length++] = GlobalDeclarations.TAG.ACTION_REQ;
        wrapper[length++] = 0x01;
        wrapper[length++] = GlobalDeclarations.TAG.SET_REQ;
        System.arraycopy(IC, 0, wrapper, length, 2);
        System.arraycopy(OBIS, 0, wrapper, length + 2, 6);
        System.arraycopy(Attribute, 0, wrapper, length + 8, 2);
        length = length + 8 + 2;
        wrapper[length++] = 0x09;
        wrapper[length++] = S2c.length;
        System.arraycopy(S2c, 0, wrapper, length, S2c.length);
        length = length + S2c.length;
        System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
        int[] plaintext = new int[31];
        System.arraycopy(wrapper, 15, plaintext, 0, 31);
        AESGCM.Aes_Gcm_Decrypt_Encrypt(31, GlobalDeclarations.Enkey, systemtitle, plaintext, 17, GlobalDeclarations.Authkey, chipertext, auth_tag, Method);
        //GlobalDeclarations.dataprint("\nchipertext", chipertext, chipertext.length);
        System.arraycopy(chipertext, 0, wrapper, 15, 31);
        System.arraycopy(auth_tag, 0, wrapper, 15 + 31, 12);
       // GlobalDeclarations.dataprint("\nAARE Request", wrapper, wrapper.length);
        ByteBuf byteBuf = Unpooled.buffer(wrapper.length);
        for(int i=0;i<wrapper.length;i++)
        {
            byteBuf.writeByte((byte)wrapper[i]);
         //   System.out.println((byte)wrapper[i]);
        }
        ctx.writeAndFlush(byteBuf);
        return wrapper;
    }

    
    public static int[] APDUFRAME_GET(int[] RequestType,int[] FrameRequestMethod,int [] systemtitle, int[] OBIS, int[] IC, int[] Attribute, long framecounter, int Method,ChannelHandlerContext ctx)
    {
    	int[] plaintext = new int[13];
    	int[] chipertext = new int[plaintext.length];
    	System.arraycopy(FrameRequestMethod, 0, plaintext, 0, 3);
    	System.arraycopy(IC, 0, plaintext, 3, 2);
    	System.arraycopy(OBIS, 0, plaintext, 5, 6);
    	System.arraycopy(Attribute, 0, plaintext, 11, 2);

    	System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
        AESGCM.Aes_Gcm_Decrypt_Encrypt(plaintext.length, GlobalDeclarations.Enkey, systemtitle, plaintext, 17, GlobalDeclarations.Authkey, chipertext, GlobalDeclarations.ENTAG, Method);
      //  GlobalDeclarations.dataprint("\nplaintext", plaintext, plaintext.length);
       // GlobalDeclarations.dataprint("\nchipertext", chipertext, plaintext.length);

int addationbyte =0;
        int[] wrapper = new int[plaintext.length+ GlobalDeclarations.ENTAG.length+6+9+addationbyte];
        int length = 6;
        int[] starting_frame = { 0x00, 0x01, 0x00, 0x30, 0x00, 0x01 };


        System.arraycopy(starting_frame, 0, wrapper, 0, 6);
        wrapper[length++] = 0x00;
        wrapper[length++] =  (plaintext.length+ GlobalDeclarations.ENTAG.length+ length-1); //length
        wrapper[length++] = (int)RequestType[0];
        wrapper[length++] =(plaintext.length + GlobalDeclarations.ENTAG.length + 5); ; // length 
        wrapper[length++] = 0x30;
        System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
        System.arraycopy(systemtitle, 8, wrapper, length, 4);
        length = length + 4;
        System.arraycopy(chipertext, 0, wrapper, length, chipertext.length);
        System.arraycopy(GlobalDeclarations.ENTAG, 0, wrapper, length + chipertext.length, 12);
       // wrapper[0]=wrapper.length-1;
      //  GlobalDeclarations.dataprint("\nAPDU Request", wrapper, wrapper.length);
        ByteBuf byteBuf = Unpooled.buffer(wrapper.length);
        for(int i=0;i<wrapper.length;i++)
        {
            byteBuf.writeByte((byte)wrapper[i]);
          //  System.out.println((byte)wrapper[i]);
        }
        ctx.writeAndFlush(byteBuf);
        return wrapper;
        
    }

    public static int[] APDUEXECUTE(int[] RequestType, int[] FrameRequestMethod, int[] systemtitle, int[] OBIS, int[] IC, int[] Attribute, int[] data, long framecounter, int Method ,ChannelHandlerContext ctx)
    {

        int[] plaintext = new int[FrameRequestMethod.length+IC.length+OBIS.length+Attribute.length+data.length];
        int[] chipertext = new int[plaintext.length];
        System.arraycopy(FrameRequestMethod, 0, plaintext, 0, 3);
        System.arraycopy(IC, 0, plaintext, 3, 2);
        System.arraycopy(OBIS, 0, plaintext, 5, 6);
        System.arraycopy(Attribute, 0, plaintext, 11, 2);
        System.arraycopy(data, 0, plaintext, 13, data.length);

        System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
        AESGCM.Aes_Gcm_Decrypt_Encrypt(plaintext.length, GlobalDeclarations.Enkey, systemtitle, plaintext, 17, GlobalDeclarations.Authkey, chipertext, GlobalDeclarations.ENTAG, Method);
        GlobalDeclarations.dataprint("plaintext", plaintext, plaintext.length);
        GlobalDeclarations.dataprint("chipertext", chipertext, plaintext.length);


        int[] wrapper = new int[plaintext.length + GlobalDeclarations.ENTAG.length + 6 + 9];
        int length = 6;
        int[] starting_frame = { 0x00, 0x01, 0x00, 0x30, 0x00, 0x01 };


        System.arraycopy(starting_frame, 0, wrapper, 0, length);
        wrapper[length++] = 0x00;
        wrapper[length++] = (int)(plaintext.length + GlobalDeclarations.ENTAG.length + length - 1); //length
        wrapper[length++] = (int)RequestType[0];
        wrapper[length++] = (int)(plaintext.length + GlobalDeclarations.ENTAG.length + 5); ; // length
        wrapper[length++] = 0x30;
        System.arraycopy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
        System.arraycopy(systemtitle, 8, wrapper, length, 4);
        length = length + 4;
        System.arraycopy(chipertext, 0, wrapper, length, chipertext.length);
        System.arraycopy(GlobalDeclarations.ENTAG, 0, wrapper, length + chipertext.length, 12);

       // GlobalDeclarations.dataprint("wrapper", wrapper, wrapper.length);
        ByteBuf byteBuf = Unpooled.buffer(wrapper.length);
        for(int i=0;i<wrapper.length;i++)
        {
            byteBuf.writeByte((byte)wrapper[i]);
           //   System.out.print((byte)wrapper[i]&0xff);
        }
        ctx.writeAndFlush(byteBuf);
        return wrapper;

    }
/*
    public static int[] APDUFRAME_BLOCK(int[] blockreques,int[] RequestType, int[] FrameRequestMethod, int[] systemtitle, long framecounter, int Method) {

        byte[] plaintext = new int[7];
        byte[] chipertext = new byte[plaintext.length];
        Array.Copy(FrameRequestMethod, 0, plaintext, 0, 3);
        Array.Copy(blockreques, 0, plaintext, 3, 4);
        Array.Copy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
        NewDLMSEncDec.Aes_Gcm_Decrypt_Encrypt(plaintext.length, GlobalDeclarations.Enkey, systemtitle, plaintext, 17, GlobalDeclarations.Authkey, chipertext, GlobalDeclarations.ENTAG, Method);
        GlobalDeclarations.dataprint("plaintext", plaintext, plaintext.Length);
        GlobalDeclarations.dataprint("chipertext", chipertext, plaintext.Length);


        byte[] wrapper = new byte[plaintext.Length + GlobalDeclarations.ENTAG.Length + 6 + 9];
        int length = 6;
        byte[] starting_frame = {0x00, 0x01, 0x00, 0x30, 0x00, 0x01};


        Array.Copy(starting_frame, 0, wrapper, 0, length);
        wrapper[length++] = 0x00;
        wrapper[length++] = (byte) (plaintext.Length + GlobalDeclarations.ENTAG.Length + length - 1); //length
        wrapper[length++] = RequestType[0];
        wrapper[length++] = (byte) (plaintext.Length + GlobalDeclarations.ENTAG.Length + 5);
        ; // length
        wrapper[length++] = 0x30;
        Array.Copy(GlobalDeclarations.long2hexbyte(framecounter), 0, systemtitle, 8, 4);
        Array.Copy(systemtitle, 8, wrapper, length, 4);
        length = length + 4;
        Array.Copy(chipertext, 0, wrapper, length, chipertext.Length);
        Array.Copy(GlobalDeclarations.ENTAG, 0, wrapper, length + chipertext.Length, 12);

        GlobalDeclarations.dataprint("wrapper", wrapper, wrapper.Length);
        return wrapper;

    }
    */
    public void sendMessage(OutputStream out, int[] msg) throws IOException {
        int count = 0;
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(msg.length);
        System.out.println("Message sent: ");
        for (int e : msg) {
            dataOut.writeInt(e);
            System.out.print(e + " ");
            if(count % 2 == 1)
                System.out.print("\n");
            count++;
        }
        dataOut.flush();
    }

}
