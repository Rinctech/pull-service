package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import common.CallbackProcessor;
/**
 * Sends one message when a connection is open and echoes back any received
 * data to the server.  Simply put, the TCP client initiates the ping-pong
 * traffic between the TCP client and server by sending the first message to
 * the server.
 */
public final class TcpClient {

	String host;
	int port;
    ChannelHandler clientHandler;

    OndemandParameters cmd=new OndemandParameters();
    
    public TcpClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	protected void execute(){
    	if(clientHandler == null){
    		throw new IllegalArgumentException("clientHandler is NULL, please define a tcpClientChannelHandler !");
    	}
    	
    	// Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             // Configure the connect timeout option.
             .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     // Decoder
                  //   p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));

                     // Encoder
                  //   p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                     
                     // the handler for client
                     p.addLast(clientHandler);
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
                                  
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();            
        } catch (Exception e){   
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.        	
            group.shutdownGracefully();
        }
    }
    
    public TcpClient buildHandler(OndemandParameters cmd, CallbackProcessor asynchCall) throws Exception{
        int [] test={0x24,0x24};
    	clientHandler = new TcpClientHandler(cmd,test, asynchCall);

    	return this;
    }        
    
   /* public static void main(String[] args) throws Exception {

int i=4;
       //   for(int i=1;i<6;i++)
            {
            OndemandParameters cmd = new OndemandParameters();
            if (i == 1)
                cmd.MeterIP = "2402:3a80:1700:187::2";
            else  if (i == 2)
                cmd.MeterIP = "2402:3a80:1700:185::2";
            else  if (i == 3)
               cmd.MeterIP = "2402:3a80:1700:181::2";
           else  if (i == 4)
                cmd.MeterIP = "2402:3a80:1700:175::2";
           else
                cmd.MeterIP = "2402:3a80:1700:186::2";
            cmd.MeterPort = 4059;
                cmd.start = System.currentTimeMillis();
            //OndemandParameters cmd = new OndemandParameters();
            Client client = new Client(cmd);
            Thread t1 = new Thread(client);
            t1.start();
        }

    }*/
}