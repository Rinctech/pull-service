package org.example;

public class AESGCM {

	
	 static class Globals
     {
         // global int
         public static int counter;
         public static int v;
         //public static byte[] RBlock = { 0xE1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
         //   public static byte[] BlockHash = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

         public static int[] sbox =   { 
             //0     1    2      3     4    5     6     7      8    9     A      B    C     D     E     F
             0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76, //0
             0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0, //1
             0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15, //2
             0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75, //3
             0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84, //4
             0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf, //5
             0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8, //6
             0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2, //7
             0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73, //8
             0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb, //9
             0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79, //A
             0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08, //B
             0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a, //C
             0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e, //D
             0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf, //E
             0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16 }; //F  

         public static int[] rsbox ={
               0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb
             , 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb
             , 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e
             , 0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25
             , 0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92
             , 0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84
             , 0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06
             , 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b
             , 0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73
             , 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e
             , 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b
             , 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4
             , 0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f
             , 0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef
             , 0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61
             , 0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d };
         public static int[] Rcon = {
             0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36};
         // global function

     }
		
		public static void Revesre4Bytes(int Buffer[],long counter)
		{
			 Buffer[0] =(int)(counter >> 24); // fourth byte
		     Buffer[1] = (int)(counter >> 16); // third byte
		     Buffer[2] =(int) (counter >> 8); // second byte
		     Buffer[3] = (int)counter; // last byte is already in proper position
		 
		}


		public static void Aes_Gcm_Decrypt_Encrypt( int DataLength,int Key[],int InitVector[],int PlainText[],int AAD_Length,int AAD[],int CipherText[],int Tag[],int Method)
		{
	        int[] SBlock = new int[1024];

		int JBlock[] = new int[16];
		int BlockCipher[] = new int[16];
		long Invoc_counter = 1;
		   int u=0,v=0,SLen;
		   long TempLong;
		   int zerobyte[]= {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		   int buffer [] = new int[4];
		  int i=0;
		  Invoc_counter=1;
		 // memcpy(JBlock,InitVector,12);
		   System.arraycopy(InitVector, 0, JBlock, 0, 12) ;
//		  for(i=0;i<12;i++)
//		   System.out.print(String.format("%02x ", JBlock[i]));
		   Revesre4Bytes(buffer, Invoc_counter);
		   System.arraycopy(buffer, 0, JBlock, 12, 4);

//		   for(i=0;i<12;i++)
//			   System.out.print(String.format("%02x ", JBlock[i]));
		   System.arraycopy(InitVector, 0, BlockCipher, 0, 12);
		   
		   Invoc_counter += 1;
		   Revesre4Bytes(buffer, Invoc_counter);
		   System.arraycopy(buffer, 0, BlockCipher, 12, 4);
		   
		   
//		   System.out.print( "\nPlainText ");
//		   for(i=0;i<DataLength;i++)
//			   System.out.print(String.format("%02x ", PlainText[i]));
	//
//		   System.out.print( "\nCipherText ");
//		   for(i=0;i<DataLength;i++)
//			   System.out.print(String.format("%02x ", CipherText[i]));
//		   
//		   System.out.print( "\nBlockCipher ");
//		   for(i=0;i<16;i++)
//			   System.out.print(String.format("%02x ", BlockCipher[i]));
//		   
		  GCTR1(DataLength,Key,Invoc_counter,InitVector,BlockCipher,PlainText,CipherText);

		  if ((DataLength % 16) > 0)
		    u=16-(DataLength%16);

		  if ((AAD_Length % 16) > 0)
		    v=16-(AAD_Length%16);

		  System.arraycopy(AAD, 0, SBlock, 0, AAD_Length);
		    for (i = AAD_Length; i < v; i++)
	            SBlock[i] = 0;



		    if (Method == 1)
		    {
		    	 System.arraycopy(CipherText, 0, SBlock, (AAD_Length + v), DataLength);
		    	// System.out.print( "\nENCRYPT\n");
		    }
	        else
	        {
	        	//  System.out.print( "\nDECRYPT \n");
	        	 System.arraycopy(PlainText, 0, SBlock, (AAD_Length + v), DataLength);
	        }

		   for (i = (AAD_Length + v + DataLength); i < (u + 4); i++)
	          SBlock[i] = 0;
		  
		  SLen=AAD_Length+DataLength+u+v+4;
		  TempLong = AAD_Length*8;
		  Revesre4Bytes(buffer, TempLong);
		  System.arraycopy(buffer, 0, SBlock, SLen, 4);
		  SLen += 4;
		  TempLong = DataLength*8;
		    for (i = SLen; i < (SLen + 4); i++)
	            SBlock[i] = 0;
		  Revesre4Bytes(buffer, TempLong);
		  System.arraycopy(buffer, 0, SBlock, SLen + 4, 4);
		  SLen += 8;
		  
//		  System.out.print( "\nkey ");
//		  for(i=0;i<16;i++)
//			   System.out.print(String.format("%02x ", Key[i]));
//		  
//		  System.out.print( "\nBlockCipher ");
//		  for(i=0;i<16;i++)
//			   System.out.print(String.format("%02x ", BlockCipher[i]));
	//
//		  
//		  System.out.print( "\nSBlock ");
//		  for(i=0;i<SLen;i++)
//			   System.out.print(String.format("%02x ", SBlock[i]));
//		  
//		  
		  
		  
		  GHASH2(SLen,Key,BlockCipher,SBlock);
		  
//		  System.out.print( "\nkey ");
//		  for(i=0;i<16;i++)
//			   System.out.print(String.format("%02x ", Key[i]));
//		  
//		  System.out.print( "\nBlockCipher ");
//		  for(i=0;i<16;i++)
//			   System.out.print(String.format("%02x ", BlockCipher[i]));

//		  for(i=0;i<24;i++)
//		   System.out.print(String.format("%02x ", PlainText[i]));
	// 
	// for(i=0;i<24;i++)
//		   System.out.print(String.format("%02x ", CipherText[i]));
		  
		  
		  GCTR1(16,Key,Invoc_counter,InitVector,JBlock,BlockCipher,BlockCipher);
		  System.arraycopy(BlockCipher, 0, Tag, 0, 12);
		}

		  public static void GHASH2(int Length, int[] Key, int[] BlockCiph, int[] PlainText)
	      {
	          int[] BlockHash = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	          int a = 0, b = 0, c = 0, i = 0;

	              a = Length / 16;
	              if ((Length % 16) > 0)
	                  a += 1;
	              aes_encrypt(BlockHash, Key);
	              
//	        	  System.out.print( "\nBlockCipher ");
//	        	  for(i=0;i<16;i++)
//	        		   System.out.print(String.format("%02x ", BlockHash[i]));

	              if (Length > 0)
	              {
	                  for (i = 0; i < 16; i++)
	                  {
	                      BlockCiph[i] = 0;
	                  }

	                  for (b = 0; b < a; b++)
	                  {
	                	//  System.out.print( "\n Started ");
	                      for (c = 0; c < 16; c++)
	                      {
	                          BlockCiph[c] = ((PlainText[b * 16 + c] ^ BlockCiph[c])& 0xff);
	                   //   System.out.print(String.format("%02x ", BlockCiph[c]));
	                      }
	                      Multiplication(BlockCiph, BlockHash, BlockCiph);

	                  }

	              }

	      }

		public static void GCTR1(int Length, int[] Key, long Invoccntr, int[] InitVector, int[] BlockCiph, int[] PlainText, int[] CipherText)
		{
		    int aa = 0;
		    int a, b, c;
		    int[] buffer = new int[5];

	int i=0;
		        a = (int)(Length / 16);
		        if ((Length % 16) > 0)
		            a += 1;

		        if (Length > 0)
		        {
		            for (b = 0; b < a; b++)
		            {
		            	
//		            	   System.out.print( "\nKey ");
//		            	   for(i=0;i<16;i++)
//		            		   System.out.print(String.format("%02x ", Key[i]));
//		            	   
//		            	   System.out.print( "\nBlockCipher ");
//		            	   for(i=0;i<16;i++)
//		            		   System.out.print(String.format("%02x ", BlockCiph[i]));
//		            	   
		                aes_encrypt(BlockCiph, Key);
//		                System.out.print( "\nBlockCipher ");
//		            	   for(i=0;i<16;i++)
//		            		   System.out.print(String.format("%02x ", BlockCiph[i]));
//		            	   System.out.print( "\nData ");
		                // dataprint("aes_encrypt", BlockCiph, 12);
		                for (c = 0; c < 16; c++)
		                {
	                         if((b * 16 + c)<Length)
		                    CipherText[b * 16 + c] = ((PlainText[b * 16 + c] ^ BlockCiph[c]) & 0xff);
		                  //  System.out.print(String.format("%02x ", CipherText[b * 16 + c] ));
		                }
		                Invoccntr += 1;
		                System.arraycopy(InitVector, 0, BlockCiph, 0, 12);
		                Revesre4Bytes(buffer, Invoccntr);
		                System.arraycopy(buffer, 0, BlockCiph, 12, 4);
		                // dataprint("BlockCiph g", BlockCiph, 16);
		                /*increment32(Invoccntr);
		                memcpy(BlockCiph, InitVector, 12);
		                Revesre4Bytes((BlockCiph + 12), Invoccntr);*/

		            }
		        }
		  }
		
		
		 public static void Multiplication(int[] X, int[] Y, int[] XY)
	     {
	         //  int i = 0;
	         int[] RBlock = { 0xE1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	         int[] ZBlock = new int[16];
	         int[] VBlock = new int[16];
	        
	         //   unsigned char ZBlock[16], VBlock[16];
	         int i = 0, Xbyt = 0, Ybyt = 0, Zbyt = 0, j = 0, Vbit = 0;
	         
//	         System.out.print( "\nX ");
//	   	  for(i=0;i<16;i++)
//	   		   System.out.print(String.format("%02x ", X[i]));
//	   	  
//	   	System.out.print( "\nY ");
//		  for(i=0;i<16;i++)
//			   System.out.print(String.format("%02x ", Y[i]));
//		  
//		  System.out.print( "\nXY ");
//		  for(i=0;i<16;i++)
//			   System.out.print(String.format("%02x ", XY[i]));
	         
		  
		  
	         for (i = 0; i < 16; i++)
	         {
	             ZBlock[i] = 0;
	             VBlock[i] = Y[i];
	         }
	         // memset(ZBlock, 0, 16);
	         // memcpy(VBlock, Y, 16);

	         for (i = 0; i < 128; i++)
	         {
	             //Zbyt = Zbyt*(X + ney(i / 8));
	             Zbyt = X[i / 8];
	             if ((Zbyt & (0x80 >> (i % 8))) > 0)
	             {
	                 for (j = 0; j < 16; j++)
	                     ZBlock[j] = (ZBlock[j] ^ VBlock[j]);
	             }

	             if ((VBlock[15] & 0x01) > 0)
	                 Vbit = 1;
	             else
	                 Vbit = 0;

	             for (j = 0; j < 16; j++)
	             {
	                 Ybyt = VBlock[15 - j];
	                 Ybyt >>= 1;
	                 if (j < 15)
	                 {
	                     Xbyt = VBlock[14 - j];
	                     if ((Xbyt & 0x01) > 0)
	                         Ybyt |= 0x80;
	                 }

	                 VBlock[15 - j] = Ybyt;
	             }

	             if ((Vbit > 0))
	             {
	                 for (j = 0; j < 16; j++)
	                     VBlock[j] = (VBlock[j] ^ RBlock[j]);
	             }
	         }

	         for (j = 0; j < 16; j++)
	             XY[j] = ZBlock[j];
	         
//	   	  System.out.print( "\nXY ");
//	   	  for(i=0;i<16;i++)
//	   		   System.out.print(String.format("%02x ", XY[i]));
	   	  
	   	  
	     }
	     public static void aes_encrypt(int[] state, int[] key)
	     {
	         int[] expandedKey = new int[176];

	         expandKey(expandedKey, key);       // expand the key into 176 bytes
	         aes_encr(state, expandedKey);
	     }

	     // decrypt
	     public static void aes_decrypt(int[] state, int[] key)
	     {
	         int[] expandedKey = new int[176];

	         expandKey(expandedKey, key);       // expand the key into 176 bytes
	         aes_decr(state, expandedKey);
	     }

	     
	     public static void expandKey(int[] expandedKey, int[] key)
	     {
	         int ii = 0, buf1 = 0;
	         for (ii = 0; ii < 16; ii++)
	             expandedKey[ii] = key[ii];
	         // dataprint("expandedKey   ", expandedKey, 12);
	         for (ii = 1; ii < 11; ii++)
	         {
	             buf1 = expandedKey[ii * 16 - 4];
	             expandedKey[ii * 16 + 0] = (Globals.sbox[expandedKey[ii * 16 - 3]] ^ expandedKey[(ii - 1) * 16 + 0] ^ Globals.Rcon[ii]);
	             expandedKey[ii * 16 + 1] = (Globals.sbox[expandedKey[ii * 16 - 2]] ^ expandedKey[(ii - 1) * 16 + 1]);
	             expandedKey[ii * 16 + 2] = (Globals.sbox[expandedKey[ii * 16 - 1]] ^ expandedKey[(ii - 1) * 16 + 2]);
	             expandedKey[ii * 16 + 3] = (Globals.sbox[buf1] ^ expandedKey[(ii - 1) * 16 + 3]);
	             expandedKey[ii * 16 + 4] =(expandedKey[(ii - 1) * 16 + 4] ^ expandedKey[ii * 16 + 0]);
	             expandedKey[ii * 16 + 5] = (expandedKey[(ii - 1) * 16 + 5] ^ expandedKey[ii * 16 + 1]);
	             expandedKey[ii * 16 + 6] = (expandedKey[(ii - 1) * 16 + 6] ^ expandedKey[ii * 16 + 2]);
	             expandedKey[ii * 16 + 7] = (expandedKey[(ii - 1) * 16 + 7] ^ expandedKey[ii * 16 + 3]);
	             expandedKey[ii * 16 + 8] = (expandedKey[(ii - 1) * 16 + 8] ^ expandedKey[ii * 16 + 4]);
	             expandedKey[ii * 16 + 9] = (expandedKey[(ii - 1) * 16 + 9] ^ expandedKey[ii * 16 + 5]);
	             expandedKey[ii * 16 + 10] = (expandedKey[(ii - 1) * 16 + 10] ^ expandedKey[ii * 16 + 6]);
	             expandedKey[ii * 16 + 11] = (expandedKey[(ii - 1) * 16 + 11] ^ expandedKey[ii * 16 + 7]);
	             expandedKey[ii * 16 + 12] = (expandedKey[(ii - 1) * 16 + 12] ^ expandedKey[ii * 16 + 8]);
	             expandedKey[ii * 16 + 13] = (expandedKey[(ii - 1) * 16 + 13] ^ expandedKey[ii * 16 + 9]);
	             expandedKey[ii * 16 + 14] = (expandedKey[(ii - 1) * 16 + 14] ^ expandedKey[ii * 16 + 10]);
	             expandedKey[ii * 16 + 15] = (expandedKey[(ii - 1) * 16 + 15] ^ expandedKey[ii * 16 + 11]);
	         }
	         // dataprint("expandedKey   ", expandedKey, 12);
//	         for(int i=0;i<174;i++)
//	         {
//	         	System.out.print(String.format("%02X ", expandedKey[i]));
//	         }
	         

	     }
	     
	     
	     public static void aes_encr(int[] state, int[] expandedKey)
	     {
	         int buf1 = 0, buf2 = 0, buf3 = 0, round = 0;
	int i=0;
	//System.out.print("\n");
//	         for( i=0;i<174;i++)
//	         {
//	         	System.out.print(String.format("%02X ", expandedKey[i]));
//	         }
//	         System.out.print("\n");
//	         
//	         for( i=0;i<16;i++)
//	         {
//	         	System.out.print(String.format("%02X ", state[i]));
//	         }
//	         
	         for (round = 0; round < 9; round++)
	         {
	             // addroundkey, sbox and shiftrows
//	        	  System.out.print("\nData : ");
//	        	 System.out.print(String.format("%02X ", (state[0] ^ expandedKey[(round * 16)])));	 
//	        	 System.out.print(String.format("%02X ", ((state[4] ^ expandedKey[(round * 16) + 4]))));
//	        	 System.out.print(String.format("%02X ",(state[8] ^ expandedKey[(round * 16) + 8])));
//	        	 System.out.print(String.format("%02X ", (state[12] ^ expandedKey[(round * 16) + 12])));
	        	// byte test = ( byte)(state[0] ^ expandedKey[(round * 16)]);
	        	 //System.out.print(String.format("%02X ",  Globals.sbox[ test]));
	             // row 0
	             state[0] = Globals.sbox[ (state[0] ^ expandedKey[(round * 16)])];
	             state[4] = Globals.sbox[ (state[4] ^ expandedKey[(round * 16) + 4])];
	             state[8] = Globals.sbox[ (state[8] ^ expandedKey[(round * 16) + 8])];
	             state[12] = Globals.sbox[ (state[12] ^ expandedKey[(round * 16) + 12])];
	             
//	             System.out.print(String.format("%02X ",   state[0] ));
//	             System.out.print(String.format("%02X ",   state[4] ));
//	             System.out.print(String.format("%02X ",   state[8] ));
//	             System.out.print(String.format("%02X ",   state[12] ));
//	             
	             // row 1
	             buf1 = state[1] ^ expandedKey[(round * 16) + 1];
	             state[1] = Globals.sbox[ (state[5] ^ expandedKey[(round * 16) + 5])];
	             state[5] = Globals.sbox[ (state[9] ^ expandedKey[(round * 16) + 9])];
	             state[9] = Globals.sbox[ (state[13] ^ expandedKey[(round * 16) + 13])];
	             state[13] = Globals.sbox[buf1];
	             
//	             System.out.print(String.format("%02X ",   state[1] ));
//	             System.out.print(String.format("%02X ",   state[5] ));
//	             System.out.print(String.format("%02X ",   state[9] ));
//	             System.out.print(String.format("%02X ",   state[13] ));
	             // row 2
	             buf1 = state[2] ^ expandedKey[(round * 16) + 2];
	             buf2 = state[6] ^ expandedKey[(round * 16) + 6];
	             state[2] = Globals.sbox[ (state[10] ^ expandedKey[(round * 16) + 10])];
	             state[6] = Globals.sbox[ (state[14] ^ expandedKey[(round * 16) + 14])];
	             state[10] = Globals.sbox[buf1];
	             state[14] = Globals.sbox[buf2];
	             
//	             System.out.print(String.format("%02X ",   state[2] ));
//	             System.out.print(String.format("%02X ",   state[6] ));
//	             System.out.print(String.format("%02X ",   state[10] ));
//	             System.out.print(String.format("%02X ",   state[14] ));
	             
	             // row 3
	             buf1 = state[15] ^ expandedKey[(round * 16) + 15];
	             state[15] = Globals.sbox[(state[11] ^ expandedKey[(round * 16) + 11])];
	             state[11] = Globals.sbox[(state[7] ^ expandedKey[(round * 16) + 7])];
	             state[7] = Globals.sbox[(state[3] ^ expandedKey[(round * 16) + 3])];
	             state[3] = Globals.sbox[buf1];
//	             System.out.print(String.format("%02X ",   state[15] ));
//	             System.out.print(String.format("%02X ",   state[11] ));
//	             System.out.print(String.format("%02X ",   state[7] ));
//	             System.out.print(String.format("%02X ",   state[3] ));
	             // mixcolums //////////
	             // col1
	             buf1 = state[0] ^ state[1] ^ state[2] ^ state[3];
	             buf2 = state[0];
	             buf3 = state[0] ^ state[1]; buf3 = galois_mul2(buf3); state[0] = (state[0] ^ buf3 ^ buf1);
	             buf3 = state[1] ^ state[2]; buf3 = galois_mul2(buf3); state[1] = (state[1] ^ buf3 ^ buf1);
	             buf3 = state[2] ^ state[3]; buf3 = galois_mul2(buf3); state[2] = (state[2] ^ buf3 ^ buf1);
	             buf3 = state[3] ^ buf2; buf3 = galois_mul2(buf3); state[3] = (state[3] ^ buf3 ^ buf1);
	             
	             // col2
	             buf1 = state[4] ^ state[5] ^ state[6] ^ state[7];
	             buf2 = state[4];
	             buf3 = state[4] ^ state[5]; buf3 = galois_mul2(buf3); state[4] = (state[4] ^ buf3 ^ buf1);
	             buf3 = state[5] ^ state[6]; buf3 = galois_mul2(buf3); state[5] = (state[5] ^ buf3 ^ buf1);
	             buf3 = state[6] ^ state[7]; buf3 = galois_mul2(buf3); state[6] = (state[6] ^ buf3 ^ buf1);
	             buf3 = state[7] ^ buf2; buf3 = galois_mul2(buf3); state[7] = (state[7] ^ buf3 ^ buf1);
	             // col3
	             buf1 = state[8] ^ state[9] ^ state[10] ^ state[11];
	             buf2 = state[8];
	             buf3 = state[8] ^ state[9]; buf3 = galois_mul2(buf3); state[8] = (state[8] ^ buf3 ^ buf1);
	             buf3 = state[9] ^ state[10]; buf3 = galois_mul2(buf3); state[9] = (state[9] ^ buf3 ^ buf1);
	             buf3 = state[10] ^ state[11]; buf3 = galois_mul2(buf3); state[10] = (state[10] ^ buf3 ^ buf1);
	             buf3 = state[11] ^ buf2; buf3 = galois_mul2(buf3); state[11] = (state[11] ^ buf3 ^ buf1);
	             // col4
	             buf1 = state[12] ^ state[13] ^ state[14] ^ state[15];
	             buf2 = state[12];
	             buf3 = state[12] ^ state[13]; buf3 = galois_mul2(buf3); state[12] = (state[12] ^ buf3 ^ buf1);
	             buf3 = state[13] ^ state[14]; buf3 = galois_mul2(buf3); state[13] = (state[13] ^ buf3 ^ buf1);
	             buf3 = state[14] ^ state[15]; buf3 = galois_mul2(buf3); state[14] = (state[14] ^ buf3 ^ buf1);
	             buf3 = state[15] ^ buf2; buf3 = galois_mul2(buf3); state[15] = (state[15] ^ buf3 ^ buf1);
	             
	             
	            for(i=0;i<16;i++)
	            	 state[i] &= (0xffL);
	             
	             
//	             System.out.print(String.format("%02X ",   state[0] ));
//	             System.out.print(String.format("%02X ",   state[1] ));
//	             System.out.print(String.format("%02X ",   state[2] ));
//	             System.out.print(String.format("%02X ",   state[3] ));
//	             System.out.print(String.format("%02X ",   state[4] ));
//	             System.out.print(String.format("%02X ",   state[5] ));
//	             System.out.print(String.format("%02X ",   state[6] ));
//	             System.out.print(String.format("%02X ",   state[7] ));
//	             System.out.print(String.format("%02X ",   state[8] ));
//	             System.out.print(String.format("%02X ",   state[9] ));
//	             System.out.print(String.format("%02X ",   state[10] ));
//	             System.out.print(String.format("%02X ",   state[11] ));
//	             System.out.print(String.format("%02X ",   state[12] ));
//	             System.out.print(String.format("%02X ",   state[13] ));
//	             System.out.print(String.format("%02X ",   state[14] ));
//	             System.out.print(String.format("%02X \n",   state[15] ));
	             
	             

	         }
	         // 10th round without mixcols
	         state[0] = Globals.sbox[(state[0] ^ expandedKey[(round * 16)])];
	         state[4] = Globals.sbox[(state[4] ^ expandedKey[(round * 16) + 4])];
	         state[8] = Globals.sbox[(state[8] ^ expandedKey[(round * 16) + 8])];
	         state[12] = Globals.sbox[(state[12] ^ expandedKey[(round * 16) + 12])];
//	         System.out.print(String.format("%02X ",   state[0] ));
//	         System.out.print(String.format("%02X ",   state[4] ));
//	         System.out.print(String.format("%02X ",   state[8] ));
//	         System.out.print(String.format("%02X ",   state[12] ));
	         // row 1
	         buf1 = state[1] ^ expandedKey[(round * 16) + 1];
	         state[1] = Globals.sbox[(state[5] ^ expandedKey[(round * 16) + 5])];
	         state[5] = Globals.sbox[(state[9] ^ expandedKey[(round * 16) + 9])];
	         state[9] = Globals.sbox[(state[13] ^ expandedKey[(round * 16) + 13])];
	         state[13] = Globals.sbox[buf1];
//	         System.out.print(String.format("%02X ",   state[1] ));
//	         System.out.print(String.format("%02X ",   state[5] ));
//	         System.out.print(String.format("%02X ",   state[9] ));
//	         System.out.print(String.format("%02X ",   state[13] ));
	         // row 2
	         buf1 = state[2] ^ expandedKey[(round * 16) + 2];
	         buf2 = state[6] ^ expandedKey[(round * 16) + 6];
	         state[2] = Globals.sbox[(state[10] ^ expandedKey[(round * 16) + 10])];
	         state[6] = Globals.sbox[(state[14] ^ expandedKey[(round * 16) + 14])];
	         state[10] = Globals.sbox[buf1];
	         state[14] = Globals.sbox[buf2];
//	         System.out.print(String.format("%02X ",   state[2] ));
//	         System.out.print(String.format("%02X ",   state[6] ));
//	         System.out.print(String.format("%02X ",   state[10] ));
//	         System.out.print(String.format("%02X ",   state[14] ));
	         // row 3
	         buf1 = state[15] ^ expandedKey[(round * 16) + 15];
	         state[15] = Globals.sbox[(state[11] ^ expandedKey[(round * 16) + 11])];
	         state[11] = Globals.sbox[(state[7] ^ expandedKey[(round * 16) + 7])];
	         state[7] = Globals.sbox[(state[3] ^ expandedKey[(round * 16) + 3])];
	         state[3] = Globals.sbox[buf1];
	         
//	         System.out.print(String.format("%02X ",   state[15] ));
//	         System.out.print(String.format("%02X ",   state[11] ));
//	         System.out.print(String.format("%02X ",   state[7] ));
//	         System.out.print(String.format("%02X ",   state[3] ));
	         
	         
	         // last addroundkey
	         state[0] ^= expandedKey[160];
	         state[1] ^= expandedKey[161];
	         state[2] ^= expandedKey[162];
	         state[3] ^= expandedKey[163];
	         state[4] ^= expandedKey[164];
	         state[5] ^= expandedKey[165];
	         state[6] ^= expandedKey[166];
	         state[7] ^= expandedKey[167];
	         state[8] ^= expandedKey[168];
	         state[9] ^= expandedKey[169];
	         state[10] ^= expandedKey[170];
	         state[11] ^= expandedKey[171];
	         state[12] ^= expandedKey[172];
	         state[13] ^= expandedKey[173];
	         state[14] ^= expandedKey[174];
	         state[15] ^= expandedKey[175];
	         
//	         System.out.print(String.format("%02X ",   state[0] ));
//	         System.out.print(String.format("%02X ",   state[1] ));
//	         System.out.print(String.format("%02X ",   state[2] ));
//	         System.out.print(String.format("%02X ",   state[3] ));
//	         System.out.print(String.format("%02X ",   state[4] ));
//	         System.out.print(String.format("%02X ",   state[5] ));
//	         System.out.print(String.format("%02X ",   state[6] ));
//	         System.out.print(String.format("%02X ",   state[7] ));
//	         System.out.print(String.format("%02X ",   state[8] ));
//	         System.out.print(String.format("%02X ",   state[9] ));
//	         System.out.print(String.format("%02X ",   state[10] ));
//	         System.out.print(String.format("%02X ",   state[11] ));
//	         System.out.print(String.format("%02X ",   state[12] ));
//	         System.out.print(String.format("%02X ",   state[13] ));
//	         System.out.print(String.format("%02X ",   state[14] ));
//	         System.out.print(String.format("%02X \n",   state[15] ));

	     }


	     public static int galois_mul2(int value)
	     {
	         if ((value >> 7) > 0)
	         {
	             value = value << 1;
	             return (value ^ 0x1b);
	         }
	         else
	         {
	             return value << 1;
	         }
	     }

	     // straight foreward aes decryption implementation
	     //   the order of substeps is the exact reverse of decryption
	     //   inverse functions:
	     //       - addRoundKey is its own inverse
	     //       - rsbox is inverse of sbox
	     //       - rightshift instead of leftshift
	     //       - invMixColumns = barreto + mixColumns
	     //   no further subfunctions to save cycles for function calls
	     //   no structuring with "for (....)" to save cycles

	     public static void aes_decr(int[] state, int[] expandedKey)
	     {
	         int buf1 = 0, buf2 = 0, buf3 = 0;
	         int round;
	         round = 9;

	         // initial addroundkey
	         state[0] ^= expandedKey[160];
	         state[1] ^= expandedKey[161];
	         state[2] ^= expandedKey[162];
	         state[3] ^= expandedKey[163];
	         state[4] ^= expandedKey[164];
	         state[5] ^= expandedKey[165];
	         state[6] ^= expandedKey[166];
	         state[7] ^= expandedKey[167];
	         state[8] ^= expandedKey[168];
	         state[9] ^= expandedKey[169];
	         state[10] ^= expandedKey[170];
	         state[11] ^= expandedKey[171];
	         state[12] ^= expandedKey[172];
	         state[13] ^= expandedKey[173];
	         state[14] ^= expandedKey[174];
	         state[15] ^= expandedKey[175];

	         // 10th round without mixcols
	         state[0] = (Globals.rsbox[state[0]] ^ expandedKey[(round * 16)]);
	         state[4] = (Globals.rsbox[state[4]] ^ expandedKey[(round * 16) + 4]);
	         state[8] = (Globals.rsbox[state[8]] ^ expandedKey[(round * 16) + 8]);
	         state[12] = (Globals.rsbox[state[12]] ^ expandedKey[(round * 16) + 12]);
	         // row 1
	         buf1 = (Globals.rsbox[state[13]] ^ expandedKey[(round * 16) + 1]);
	         state[13] = (Globals.rsbox[state[9]] ^ expandedKey[(round * 16) + 13]);
	         state[9] = (Globals.rsbox[state[5]] ^ expandedKey[(round * 16) + 9]);
	         state[5] = (Globals.rsbox[state[1]] ^ expandedKey[(round * 16) + 5]);
	         state[1] = buf1;
	         // row 2
	         buf1 = (Globals.rsbox[state[2]] ^ expandedKey[(round * 16) + 10]);
	         buf2 = (Globals.rsbox[state[6]] ^ expandedKey[(round * 16) + 14]);
	         state[2] = (Globals.rsbox[state[10]] ^ expandedKey[(round * 16) + 2]);
	         state[6] = (Globals.rsbox[state[14]] ^ expandedKey[(round * 16) + 6]);
	         state[10] = buf1;
	         state[14] = buf2;
	         // row 3
	         buf1 = (Globals.rsbox[state[3]] ^ expandedKey[(round * 16) + 15]);
	         state[3] = (Globals.rsbox[state[7]] ^ expandedKey[(round * 16) + 3]);
	         state[7] = (Globals.rsbox[state[11]] ^ expandedKey[(round * 16) + 7]);
	         state[11] = (Globals.rsbox[state[15]] ^ expandedKey[(round * 16) + 11]);
	         state[15] = buf1;

	         for (round = 8; round >= 0; round--)
	         {
	             // barreto
	             //col1
	             buf1 = galois_mul2(galois_mul2(state[0] ^ state[2]));
	             buf2 = galois_mul2(galois_mul2(state[1] ^ state[3]));
	             state[0] ^= buf1; state[1] ^= buf2; state[2] ^= buf1; state[3] ^= buf2;
	             //col2
	             buf1 = galois_mul2(galois_mul2(state[4] ^ state[6]));
	             buf2 = galois_mul2(galois_mul2(state[5] ^ state[7]));
	             state[4] ^= buf1; state[5] ^= buf2; state[6] ^= buf1; state[7] ^= buf2;
	             //col3
	             buf1 = galois_mul2(galois_mul2(state[8] ^ state[10]));
	             buf2 = galois_mul2(galois_mul2(state[9] ^ state[11]));
	             state[8] ^= buf1; state[9] ^= buf2; state[10] ^= buf1; state[11] ^= buf2;
	             //col4
	             buf1 = galois_mul2(galois_mul2(state[12] ^ state[14]));
	             buf2 = galois_mul2(galois_mul2(state[13] ^ state[15]));
	             state[12] ^= buf1; state[13] ^= buf2; state[14] ^= buf1; state[15] ^= buf2;
	             // mixcolums //////////
	             // col1
	             buf1 = state[0] ^ state[1] ^ state[2] ^ state[3];
	             buf2 = state[0];
	             buf3 = state[0] ^ state[1]; buf3 = galois_mul2(buf3); state[0] = (state[0] ^ buf3 ^ buf1);
	             buf3 = state[1] ^ state[2]; buf3 = galois_mul2(buf3); state[1] = (state[1] ^ buf3 ^ buf1);
	             buf3 = state[2] ^ state[3]; buf3 = galois_mul2(buf3); state[2] =(state[2] ^ buf3 ^ buf1);
	             buf3 = state[3] ^ buf2; buf3 = galois_mul2(buf3); state[3] = (state[3] ^ buf3 ^ buf1);
	             // col2
	             buf1 = state[4] ^ state[5] ^ state[6] ^ state[7];
	             buf2 = state[4];
	             buf3 = state[4] ^ state[5]; buf3 = galois_mul2(buf3); state[4] = (state[4] ^ buf3 ^ buf1);
	             buf3 = state[5] ^ state[6]; buf3 = galois_mul2(buf3); state[5] = (state[5] ^ buf3 ^ buf1);
	             buf3 = state[6] ^ state[7]; buf3 = galois_mul2(buf3); state[6] = (state[6] ^ buf3 ^ buf1);
	             buf3 = state[7] ^ buf2; buf3 = galois_mul2(buf3); state[7] = (state[7] ^ buf3 ^ buf1);
	             // col3
	             buf1 = state[8] ^ state[9] ^ state[10] ^ state[11];
	             buf2 = state[8];
	             buf3 = state[8] ^ state[9]; buf3 = galois_mul2(buf3); state[8] = (state[8] ^ buf3 ^ buf1);
	             buf3 = state[9] ^ state[10]; buf3 = galois_mul2(buf3); state[9] = (state[9] ^ buf3 ^ buf1);
	             buf3 = state[10] ^ state[11]; buf3 = galois_mul2(buf3); state[10] =(state[10] ^ buf3 ^ buf1);
	             buf3 = state[11] ^ buf2; buf3 = galois_mul2(buf3); state[11] = (state[11] ^ buf3 ^ buf1);
	             // col4
	             buf1 = state[12] ^ state[13] ^ state[14] ^ state[15];
	             buf2 = state[12];
	             buf3 = state[12] ^ state[13]; buf3 = galois_mul2(buf3); state[12] = (state[12] ^ buf3 ^ buf1);
	             buf3 = state[13] ^ state[14]; buf3 = galois_mul2(buf3); state[13] = (state[13] ^ buf3 ^ buf1);
	             buf3 = state[14] ^ state[15]; buf3 = galois_mul2(buf3); state[14] = (state[14] ^ buf3 ^ buf1);
	             buf3 = state[15] ^ buf2; buf3 = galois_mul2(buf3); state[15] = (state[15] ^ buf3 ^ buf1);

	             // addroundkey, rsbox and shiftrows
	             // row 0
	             state[0] = (Globals.rsbox[state[0]] ^ expandedKey[(round * 16)]);
	             state[4] = (Globals.rsbox[state[4]] ^ expandedKey[(round * 16) + 4]);
	             state[8] = (Globals.rsbox[state[8]] ^ expandedKey[(round * 16) + 8]);
	             state[12] = (Globals.rsbox[state[12]] ^ expandedKey[(round * 16) + 12]);
	             // row 1
	             buf1 = (Globals.rsbox[state[13]] ^ expandedKey[(round * 16) + 1]);
	             state[13] = (Globals.rsbox[state[9]] ^ expandedKey[(round * 16) + 13]);
	             state[9] = (Globals.rsbox[state[5]] ^ expandedKey[(round * 16) + 9]);
	             state[5] = (Globals.rsbox[state[1]] ^ expandedKey[(round * 16) + 5]);
	             state[1] = buf1;
	             // row 2
	             buf1 = (Globals.rsbox[state[2]] ^ expandedKey[(round * 16) + 10]);
	             buf2 = (Globals.rsbox[state[6]] ^ expandedKey[(round * 16) + 14]);
	             state[2] = (Globals.rsbox[state[10]] ^ expandedKey[(round * 16) + 2]);
	             state[6] =(Globals.rsbox[state[14]] ^ expandedKey[(round * 16) + 6]);
	             state[10] = buf1;
	             state[14] = buf2;
	             // row 3
	             buf1 = (Globals.rsbox[state[3]] ^ expandedKey[(round * 16) + 15]);
	             state[3] = (Globals.rsbox[state[7]] ^ expandedKey[(round * 16) + 3]);
	             state[7] = (Globals.rsbox[state[11]] ^ expandedKey[(round * 16) + 7]);
	             state[11] = (Globals.rsbox[state[15]] ^ expandedKey[(round * 16) + 11]);
	             state[15] = buf1;
	         }


	     }
	}


