package org.example;

public class GlobalDeclarations {
	public static int[] Enkey = { 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30 };
    public static int[] Authkey = { 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30 };
   public static int[] HLSkey = { 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77, 0x77 };
   //public  static  int[] HLSkey ={0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x30,0x31,0x32,0x33,0x34,0x35,0x36};
    public static int[] FLAG = { 0x41,0x53,0x4D };
    public static int[] arrq = { 0x00, 0x01, 0x00, 0x30, 0x00, 0x01, 0x00, 0x5F, 0x60, 0x5D, 0xA1, 0x09, 0x06, 0x07, 0x60, 0x85, 0x74, 0x05, 0x08, 0x01, 0x03, 0xA6, 0x0A, 0x04, 0x08, 0x41, 0x53, 0x4D, 0x31, 0x32, 0x33, 0x34, 0x35, 0x8A, 0x02, 0x07, 0x80, 0x8B, 0x07, 0x60, 0x85, 0x74, 0x05, 0x08, 0x02, 0x02, 0xAC, 0x12, 0x80, 0x10, 0x23, 0x08, 0x18, 0x35, 0x58, 0x2B, 0x0F, 0x39, 0x10, 0x7E, 0x72, 0x6F, 0x15, 0x72, 0x0A, 0x78, 0xBE, 0x23, 0x04, 0x21, 0x21, 0x1F, 0x30 };
    public static int[] RQ_IVsystemlitle = { 0x41, 0x53, 0x4D, 0x31, 0x32, 0x33, 0x34, 0x35,0x00,0x00,0x00,0x00};
    public static int[] Rev_IVsystemlitle = new int[12];
    public static int[] USOBIS = { 0x00, 0x00, 0x28, 0x00, 0x00, 0xFF };
    public static int[] USIC = { 0x00, 0x0F };
    public static int[] USATTB = { 0x01, 0x01 };
    public static int[] DETAG = new int[12];
    public static int[] ENTAG = new int[12];
    public static long RequestFramecounter=0;
    
    class TAG {
    	   public final static int  TAG_AARE= 0x61;
    			public final static int   RLRE=0x63;
    			public final static int ACTION_REQ_GLO = 0xCB;
    			public final static int  GET_REQ =0xC0;
    			public final static int  SET_REQ =0xC1;
    			public final static int ACTION_REQ=0xC3;
    			public final static int ACTION_RES_GLO=0xCF;
    			public final static int  GET_REQ_GLO=0xC8;
    			public final static int  GET_RES_GLO=0xCC;
    			public final static int  GET_RES=0xC4;
    			/* Get response types */
    			public final static int  GET_RES_NORMAL=0x01;
    			public final static int  GET_RES_BLOCK=0x02;
    			public final static int  GET_RES_WITHLIST=0x03;
    	}
    
    class llength
    {
    	public final static int  AUTHTAG = 12;
    	public final static int SECURITY = 1;
    	public final static int FRAME_COUNT_LEN = 4;

    }
    
	  public static int[] long2hexbyte(long value)
      {
          int[] bytes = new int[4];
          bytes[0] = (byte)(value >> 24);
          bytes[1] = (byte)(value >> 16);
          bytes[2] = (byte)(value >> 8);
          bytes[3] = (byte)value;
          return bytes;
      }
	  
      public static void dataprint(String name, int[] data, int len)
      {
          int i = 0;
          System.out.print(name +" :");
          for( i=0;i<len;i++)
			{
				System.out.print(String.format("%02X ", data[i] ));
			}
          System.out.print("\n");
		  System.out.print("\n");
      }
      
      public static int[] hexStringTointArray(String message)
	     {
	       //int len = s.length();
	        int[] ints = new int[message.length() / 2];
	        int intindex = 0;
	        //ints = new int[message.Length / 2];
	        for (int i = 0; i < message.length(); i += 2)
	        {
	            ints[intindex] = (int)Integer.parseInt(message.substring(i, i + 2), 16);
	            intindex++;
	        }
	        return ints;
	    }

}
