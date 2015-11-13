package com.mc.vending.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具函数
<<<<<<< HEAD
 * 
=======
 *                       
>>>>>>> 745bc1cd8105c8d5705daa981dfd193e5c8901f0
 * @Filename: StringHelper.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public final class StringHelper {
<<<<<<< HEAD
	private static String hexStr = "0123456789ABCDEF";
	private static String[] binaryArray = { "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000",
			"1001", "1010", "1011", "1100", "1101", "1110", "1111" };

	/**
	 * 检查字符串是否为空
	 * <p>
	 * 为null或者长度为0视为空字符串
	 * 
	 * @param value
	 *            要检查的字符串
	 * @param trim
	 *            是否去掉头尾的特定字符
	 * @param trimChars
	 *            要去掉的特定字符
	 * @return
	 */
	public static boolean isEmpty(String value, boolean trim, char... trimChars) {
		if (trim)
			return value == null || trim(value, trimChars).length() <= 0;
		return value == null || value.length() <= 0;
	}

	/**
	 * 检查字符串是否为空
	 * <p>
	 * 为null或者长度为0视为空字符串
	 * 
	 * @param value
	 *            要检查的字符串
	 * @param trim
	 *            是否去掉头尾的空格
	 * @return
	 */
	public static boolean isEmpty(String value, boolean trim) {
		return isEmpty(value, trim, ' ');
	}

	/**
	 * 检查字符串是否为空
	 * <p>
	 * 为null或者长度为0视为空字符串
	 * 
	 * @param value
	 *            要检查的字符串
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return isEmpty(value, false);
	}

	/**
	 * 如果为null，转换为""
	 * 
	 * @param value
	 * @return
	 */
	public static String nullSafeString(String value) {
		return value == null ? "" : value;
	}

	/**
	 * 去掉头尾空格字符
	 * 
	 * @param value
	 *            待处理的字符串
	 * @return
	 */
	public static String trim(String value) {
		return trim(3, value, ' ');
	}

	/**
	 * 去除字符串头尾的特定字符
	 * 
	 * @param value
	 *            待处理的字符串
	 * @param chars
	 *            需要去掉的特定字符
	 * @return
	 */
	public static String trim(String value, char... chars) {
		return trim(3, value, chars);
	}

	/**
	 * 去除字符串头部的特定字符
	 * 
	 * @param value
	 *            待处理的字符串
	 * @param chars
	 *            需要去掉的特定字符
	 * @return
	 */
	public static String trimStart(String value, char... chars) {
		return trim(1, value, chars);
	}

	/**
	 * 去除字符串尾部的特定字符
	 * 
	 * @param value
	 *            待处理的字符串
	 * @param chars
	 *            需要去掉的特定字符
	 * @return
	 */
	public static String trimEnd(String value, char... chars) {
		return trim(2, value, chars);
	}

	/**
	 * 去掉字符串头尾特定字符
	 * 
	 * @param mode
	 *            <li>1: 去掉头部特定字符；
	 *            <li>2: 去掉尾部特定字符；
	 *            <li>3: 去掉头尾特定字符；
	 * @param value
	 *            待处理的字符串
	 * @param chars
	 *            需要去掉的特定字符
	 * @return
	 */
	private static String trim(int mode, String value, char... chars) {
		if (value == null || value.length() <= 0)
			return value;

		int startIndex = 0, endIndex = value.length(), index = 0;
		if (mode == 1 || mode == 3) {
			// trim头部
			while (index < endIndex) {
				if (contains(chars, value.charAt(index++))) {
					startIndex++;
					continue;
				}
				break;
			}
		}

		if (startIndex >= endIndex)
			return "";

		if (mode == 2 || mode == 3) {
			// trim尾部
			index = endIndex - 1;
			while (index >= 0) {
				if (contains(chars, value.charAt(index--))) {
					endIndex--;
					continue;
				}
				break;
			}
		}

		if (startIndex >= endIndex)
			return "";
		if (startIndex == 0 && endIndex == value.length() - 1)
			return value;

		return value.substring(startIndex, endIndex);
	}

	private static boolean contains(char[] chars, char chr) {
		if (chars == null || chars.length <= 0)
			return false;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == chr)
				return true;
		}
		return false;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isEmail(String email) {

		boolean flag = false;

		try {
			if (email == null) {
				return flag;
			}

			email = email.replaceAll(" ", "");
			if ("".equals(email)) {
				return flag;
			}

			flag = match(
					"^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$",
					email);

		} catch (Exception e) {
			return false;
		}
		return flag;

	}

	/**
	 * 手机号码格式检查
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isMobile(String phone) {
		boolean flag = false;

		try {
			if (phone == null) {
				return flag;
			}

			phone = phone.replaceAll(" ", "");
			if ("".equals(phone)) {
				return flag;
			}

			flag = match("^1[3|4|5|8]\\d{9}$", phone);

		} catch (Exception e) {
			return false;
		}
		return flag;
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 验证字符串是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 自动填充8位编号
	 * 
	 * @param str
	 * @return
	 */
	public static String autoCompletionCode(String str) {
		str = nullSafeString(str).trim();
		int length = str.length();
		if (length > 0 && length <= 8) {
			String tmpStr = "";
			for (int i = 0; i < 8 - length; i++) {
				tmpStr += "0";
			}
			return tmpStr + str;
		}
		return str;
	}

	/**
	 * 
	 * @param hexString
	 * @return 将十六进制转换为字节数组
	 */
	public static byte[] HexStringToBinary(String hexString) {
		// hexString的长度对2取整，作为bytes的长度
		int len = hexString.length() / 2;
		byte[] bytes = new byte[len];
		byte high = 0;// 字节高四位
		byte low = 0;// 字节低四位

		for (int i = 0; i < len; i++) {
			// 右移四位得到高位
			high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
			low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
			bytes[i] = (byte) (high | low);// 高地位做或运算
		}
		return bytes;
	}

	/**
	 * 
	 * @param str
	 * @return 转换为二进制字符串
	 */
	private static String bytes2BinaryStr(byte[] bArray) {

		String outStr = "";
		int pos = 0;
		for (byte b : bArray) {
			// 高四位
			pos = (b & 0xF0) >> 4;
			outStr += binaryArray[pos];
			// 低四位
			pos = b & 0x0F;
			outStr += binaryArray[pos];
		}
		return outStr;

	}

	/**
	 * 
	 * @param hexString
	 * @return 将十六进制转换为字符串
	 */
	public static String HexStringToBinaryString(String hexString) {
		return bytes2BinaryStr(HexStringToBinary(hexString));
	}
=======
    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     * @param value 要检查的字符串
     * @param trim 是否去掉头尾的特定字符
     * @param trimChars 要去掉的特定字符
     * @return
     */
    public static boolean isEmpty(String value, boolean trim, char... trimChars) {
        if (trim)
            return value == null || trim(value, trimChars).length() <= 0;
        return value == null || value.length() <= 0;
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     * @param value 要检查的字符串
     * @param trim 是否去掉头尾的空格
     * @return
     */
    public static boolean isEmpty(String value, boolean trim) {
        return isEmpty(value, trim, ' ');
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     * @param value 要检查的字符串
     * @return
     */
    public static boolean isEmpty(String value) {
        return isEmpty(value, false);
    }

    /**
     * 如果为null，转换为""
     * @param value
     * @return
     */
    public static String nullSafeString(String value) {
        return value == null ? "" : value;
    }

    /**
     * 去掉头尾空格字符
     * @param value 待处理的字符串
     * @return
     */
    public static String trim(String value) {
        return trim(3, value, ' ');
    }

    /**
     * 去除字符串头尾的特定字符
     * 
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trim(String value, char... chars) {
        return trim(3, value, chars);
    }

    /**
     * 去除字符串头部的特定字符
     * 
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trimStart(String value, char... chars) {
        return trim(1, value, chars);
    }

    /**
     * 去除字符串尾部的特定字符
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trimEnd(String value, char... chars) {
        return trim(2, value, chars);
    }

    /**
     * 去掉字符串头尾特定字符
     * @param mode 
     * <li>1: 去掉头部特定字符；
     * <li>2: 去掉尾部特定字符；
     * <li>3: 去掉头尾特定字符；
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    private static String trim(int mode, String value, char... chars) {
        if (value == null || value.length() <= 0)
            return value;

        int startIndex = 0, endIndex = value.length(), index = 0;
        if (mode == 1 || mode == 3) {
            // trim头部
            while (index < endIndex) {
                if (contains(chars, value.charAt(index++))) {
                    startIndex++;
                    continue;
                }
                break;
            }
        }

        if (startIndex >= endIndex)
            return "";

        if (mode == 2 || mode == 3) {
            // trim尾部
            index = endIndex - 1;
            while (index >= 0) {
                if (contains(chars, value.charAt(index--))) {
                    endIndex--;
                    continue;
                }
                break;
            }
        }

        if (startIndex >= endIndex)
            return "";
        if (startIndex == 0 && endIndex == value.length() - 1)
            return value;

        return value.substring(startIndex, endIndex);
    }

    private static boolean contains(char[] chars, char chr) {
        if (chars == null || chars.length <= 0)
            return false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == chr)
                return true;
        }
        return false;
    }

    /**
     * 验证邮箱
     * 
     * @param 待验证的字符串
     * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isEmail(String email) {

        boolean flag = false;

        try {
            if (email == null) {
                return flag;
            }

            email = email.replaceAll(" ", "");
            if ("".equals(email)) {
                return flag;
            }

            flag = match(
                "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$",
                email);

        } catch (Exception e) {
            return false;
        }
        return flag;

    }

    /**
     * 手机号码格式检查
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone) {
        boolean flag = false;

        try {
            if (phone == null) {
                return flag;
            }

            phone = phone.replaceAll(" ", "");
            if ("".equals(phone)) {
                return flag;
            }

            flag = match("^1[3|4|5|8]\\d{9}$", phone);

        } catch (Exception e) {
            return false;
        }
        return flag;
    }

    /**
     * @param regex
     *            正则表达式字符串
     * @param str
     *            要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 自动填充8位编号
     * @param str
     * @return
     */
    public static String autoCompletionCode(String str) {
        str = nullSafeString(str).trim();
        int length = str.length();
        if (length > 0 && length <= 8) {
            String tmpStr = "";
            for (int i = 0; i < 8 - length; i++) {
                tmpStr += "0";
            }
            return tmpStr + str;
        }
        return str;
    }
>>>>>>> 745bc1cd8105c8d5705daa981dfd193e5c8901f0
}