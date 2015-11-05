package com.mc.vending.tools.utils;

import java.math.BigInteger;

import com.mc.vending.tools.ZillionLog;

/**
 * 数据转换工具
 */
public class MyFunc {
    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    static public int isOdd(int num) {
        return num & 0x1;
    }

    //-------------------------------------------------------
    static public int HexToInt(String inHex)//Hex字符串转int
    {
        return Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    static public byte HexToByte(String inHex)//Hex字符串转byte
    {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }

    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
            //            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    static public String ByteArrToHex(byte[] inBytArr, boolean isSplit)//字节数组转转hex字符串
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
            if (isSplit) {
                strBuilder.append(" ");
            }
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr, int offset, int byteCount)//字节数组转转hex字符串，可选长度
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //转hex字符串转字节数组
    static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {//奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {//偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    //计算BCC值，例如02 02 45 03 44 03
    public static int getBCC(String cmd) {
        int bcc = 0;
        for (int i = 1; i <= cmd.length() / 2; i++) {
            bcc ^= Integer.parseInt(cmd.substring(i * 2 - 2, i * 2), 16);
        }
        return bcc;
    }

    //校验返回值
    public static boolean checkBCC(byte[] inBytArr) {
        int bcc = 0;
        for (int i = 1; i <= inBytArr.length - 2; i++) {
            bcc ^= inBytArr[i];
        }
        return inBytArr[inBytArr.length - 1] == bcc;
    }

    //获取RFID卡号，参数为读卡器返回值
    public static String getRFIDSerialNo(String strHex) {
        String result = "";
        try {
            byte[] arrHex;
            String Str = strHex.replaceAll("\\s*", "");
            arrHex = HexToByteArr(Str);
            //返回值不完整
            if (arrHex.length != 7) {
                result = null;
            } else {

                //帧长度错误
                if (arrHex[0] != arrHex.length - 2) {
                    result = null;
                } else {

                    //返回卡序列号
                    if (checkBCC(arrHex) && (arrHex[0] == 0x05)) {
                        String str = Str.substring(4, 12);
                        String tmp = "";
                        for (int i = str.length() - 2; i >= 0; i = i - 2) {
                            tmp += str.substring(i, i + 2);
                        }
                        result = new BigInteger(tmp, 16).toString();
                        //            return Str.substring(4, 12);
                    }
                }
            }

            if (result == null) {
                result = getIDSerialNo(strHex);
            }

        } catch (Exception e) {
            result = null;
            ZillionLog.e("getRFIDSerialNo", e.getMessage(),e);
        }
        return result;
    }

    public static String getIDSerialNo(String strHex) {
        //02 30 30 ** ** ** ** 38 35 30 30 0D 0A 03
        try {

            String Str = strHex.replaceAll("\\s*", "");
            if (!(Str.length() == 28 && Str.substring(0, 2).equals("02") && Str.substring(Str.length() - 6)
                    .equals("0D0A03"))) {
                return null;
            }
            Str = Str.substring(2, 22);
            String tmp = "";
            for (int i = 0; i <= Str.length() - 2; i = i + 2) {
                tmp += (char) Integer.parseInt(Str.substring(i, i + 2), 16);
            }
            return tmp;
        } catch (Exception e) {
            ZillionLog.e("getIDSerialNo", e.getMessage(),e);
        }
        return null;
    }

    public static void main(String[] args) {
//        String strHex = "02 30 30 30 36 38 38 30 33 35 38 0D 0A 03";
//        System.out.println(getIDSerialNo("02 30 30 30 36 38 38 30 33 35 38 0D 0A 03"));
        
        System.out.println(cmdOpenVender(1, 1));
    }

    //生成售货机控制指令，参数为命令字加数据包
    public static String getVenderCommand(String cmd) {
        String cmdString = cmd.replaceAll("\\s*", "");
        String bcc = Integer.toHexString(getBCC(cmdString));
        //        return "AA53333100000051AC";
        return ("AA" + cmdString + bcc + "AC").toUpperCase();
    }

    //生成打开售货机货道指令，参数为列，行
    public static String cmdOpenVender(int col, int row) {
        String cmdString = "53"; //开始,层高固定为000000
        cmdString += String.format("%02x%02x000000", col + 0x30, row + 0x30);

        //        cmdString += String.format("%02x", col + 48) + String.format("%02x", row + 48) + "00 00 00";
        return getVenderCommand(cmdString);
    }

    public static char convertIntToAscii(int a) {
        return (a >= 0 && a <= 255) ? (char) a : '\0';
    }

    //生成检查售货机货道指令，参数为列，行
    public static String cmdCheckVender(int col, int row) {
        String cmdString = "52"; //询问状态,层高固定为000000
        cmdString += String.format("%02x%02x000000", col + 0x30, row + 0x30);
        //        cmdString += String.format("%02x", col) + String.format("%02x", row) + "00 00 00";
        return getVenderCommand(cmdString);
    }

    //计算累加和值
    public static int getSum(String cmd) {
        int sum = 0;
        String inHex = cmd.replaceAll("\\s*", "");

        for (int i = 1; i <= cmd.length() / 2; i++) {
            sum += Integer.parseInt(inHex.substring(i * 2 - 2, i * 2), 16);
        }
        return sum;
    }

    //生成格子机控制指令 
    public static String getStoreCommand(String cmd) {
        String cmdString = cmd.replaceAll("\\s*", "");
        return (cmdString + String.format("%04x", getSum(cmdString))).toUpperCase();
    }

    //生成打开格子机指令，参数为设备型号，编号，门号
    public static String cmdOpenStoreDoor(int type, int number, int door) {
        String cmdString = String.format("%04x", type) + String.format("%04x", number) + "00 01 00 01";
        cmdString += String.format("%02x", door);
        return getStoreCommand(cmdString);
    }

    //生成检测格子机指令，参数为设备型号，编号
    public static String cmdCheckStoreDoor(int type, int number) {
        String cmdString = String.format("%04x", type) + String.format("%04x", number) + "00 02 00 01 00";
        return getStoreCommand(cmdString);
    }

    //生成检测格子机通讯，参数为设备型号，编号
    public static String cmdCheckStoreStatus(int type, int number) {
        String cmdString = String.format("%04x", type) + String.format("%04x", number) + "00 E0 00 01 00";
        return getStoreCommand(cmdString);
    }

    //常用指令
    public static final String cmdOpenKeyBoard  = "02303234353033343403";
    public static final String cmdCloseKeyBoard = "02303234353030343703";
    public static final String cmdGetSerialNo   = "01F0F0";
    public static final String cmdBeep          = "030FFF00F0";

    //    AA 53 33 31 00 00 00 51 AC

    public static String cmdOpenKeyBoard() {
        return cmdOpenKeyBoard;
    }

    public static String cmdCloseKeyBoard() {
        return cmdCloseKeyBoard;
    }

    public static String cmdGetSerialNo() {
        return cmdGetSerialNo;
    }

    public static String cmdBeep() {
        return cmdBeep;
    }

}