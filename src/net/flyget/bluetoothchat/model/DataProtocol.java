package net.flyget.bluetoothchat.model;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Micheal
 *
 */
public class DataProtocol {
	public final static byte HEAD = 0xA;
	public final static byte TYPE_MSG = 0xC;
	public final static byte TYPE_FILE = 0xF;
	
	public static byte[] packMsg(String msg) throws UnsupportedEncodingException{
		byte[] msgbytes = msg.getBytes("UTF-8");
		byte lowLen = (byte)(msgbytes.length & 0xFF);
		byte hiLen = (byte)(msgbytes.length >> 8 & 0xFF);
		byte[] buf = new byte[msgbytes.length + 4];
		buf[0] = HEAD;
		buf[1] = TYPE_MSG;
		buf[2] = hiLen;
		buf[3] = lowLen;
		System.arraycopy(msgbytes, 0, buf, 4, msgbytes.length);
		return buf;
	}
	
	public static byte[] packFile(File file) throws UnsupportedEncodingException{
		byte total0 = (byte)(file.length() & 0xFF);
		byte total1 = (byte)(file.length() >> 8  & 0xFF);
		byte total2 = (byte)(file.length() >> 16 & 0xFF);
		byte total3 = (byte)(file.length() >> 24 & 0xFF);
		
		byte[] fnamebytes = file.getName().getBytes("UTF-8");

		byte lowLen = (byte)(fnamebytes.length & 0xFF);
		byte hiLen = (byte)(fnamebytes.length >> 8 & 0xFF);
		
		byte[] buf = new byte[fnamebytes.length + 8];
		buf[0] = HEAD;
		buf[1] = TYPE_FILE;
		buf[2] = total3;
		buf[3] = total2;
		buf[4] = total1;
		buf[5] = total0;
		buf[6] = hiLen;
		buf[7] = lowLen;
		System.arraycopy(fnamebytes, 0, buf, 8, fnamebytes.length);
		return buf;
	}
	
	public static Message unpackData(byte[] data) throws UnsupportedEncodingException{
		if(data[0] != HEAD)
			return null;
		Message msg = new Message();
		switch(data[1]){
		case TYPE_FILE:
			msg.type = TYPE_FILE;
			msg.total = data[2] << 24 | data[3] << 16 | data[4] << 8 | data[5];
			msg.fileName = new String(data, 8, data[6] << 8 | data[7], "UTF-8");
			break;
		case TYPE_MSG:
			msg.type = TYPE_MSG;
			msg.length = data[2] << 8 | data[3];
			msg.msg = new String(data, 4, msg.length, "UTF-8");
			break;
		}
		return msg;
	}
}
