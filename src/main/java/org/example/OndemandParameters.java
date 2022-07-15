package org.example;

public class OndemandParameters {
		public  String MeterSerialnumber;
		public  String MeterIP;
		public  int MeterPort;
		public  String MeterFlag;
		public  String Obis;
		public  String Ic;
		public  String Attributes;
		public  String payload;
		public  String EKEY;
		public  String AKEY;
		public  String Security_Id;
	    public  String msg_type;

		public  long RequestFramecounter=0;
	    public  int[] Rev_IVsystemlitle = new int[12];
		public int Packetscount=0;
		public int i;
		public  long start;
		public  long end;
}

class RequestCommand
{
	public static int[] USOBIS = {0x01,0x00,0x5E,0x5B,0x00,0xFF};
	public static int[] USIC = { 0x00, 0x07 };
	public static int[] USATTB = { 0x02, 0x00 };
}

