package davidwang.tm.tools;

import android.graphics.Paint;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Pattern;

public class StringUtil {

    //Java字符集名称常量定义
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final String CHARSET_UTF16 = "UTF-16";
    public static final String CHARSET_Unicode = "unicode";
    public static final String CHARSET_Iso8859_1 = "ISO8859-1";
    public static final String CHARSET_BIG5 = "BIG5";

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 判断字符是否为空
     *
     * @param str
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || "".equals(str.trim()) || "null".equalsIgnoreCase(str))
            return true;
        else return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 验证是否是 合法邮箱格式
     *
     * @param email
     * @return boolean
     */
    public static boolean isIegalEmail(String email) {
        return Pattern.compile("\\w+([-_.]\\w+)*@\\w+([-_.]\\w+)*\\.\\w+([-_.]\\w+)*").matcher(email).matches();
    }

    /**
     * 验证是否存在中文字符
     *
     * @param str
     * @return boolean
     */
    public static boolean isContainsChineseCharacter(String str) {
        return Pattern.compile("[.@\\w]*[\u4e00-\u9fa5]+[.@\\w]*").matcher(str).matches();
    }

    /**
     * <![CDATA[]]>
     *
     * @param s
     * @return
     */
    public static String getCDATA(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("<![CDATA[").append(s).append("]]>");
        return sb.toString();
    }

    /**
     * string 转int，吃掉exception
     *
     * @param s
     * @return
     */
    public static int formatInt(String s) {
        int id = -1;
        try {
            id = Integer.valueOf(s);
        } catch (NumberFormatException ex) {
            id = -1;
        }

        return id;
    }

    /**
     * 返回MB
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        try {
            double d = size / (1024 * 1024 * 1024);
            DecimalFormat df2 = new DecimalFormat("#,###,###,##0.00");
            double dd2dec = new Double(df2.format(d)).doubleValue();
            return String.valueOf(dd2dec);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 替换指定字符串
     *
     * @param input
     * @param search
     * @param replacement
     * @return string
     */
    public static String replace(String input, String search, String replacement) {
        int pos = input.indexOf(search);
        if (pos != -1) {
            StringBuilder buffer = new StringBuilder();
            int lastPos = 0;
            do {
                buffer.append(input.substring(lastPos, pos)).append(replacement);
                lastPos = pos + search.length();
                pos = input.indexOf(search, lastPos);
            } while (pos != -1);
            buffer.append(input.substring(lastPos));
            input = buffer.toString();
        }
        return input;
    }

    /**
     * 汉字转码方法
     *
     * @param text 源字符串
     * @return 转码后的字符串
     */
    public static String encode(String text) {
        char[] utfBytes = text.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < utfBytes.length; i++) {
            if (utfBytes[i] == '&') {
                result.append("&amp;");
                continue;
            }
            if (utfBytes[i] == '<') {
                result.append("&lt;");
                continue;
            }
            if (utfBytes[i] == '>') {
                result.append("&gt;");
                continue;
            }
            if (utfBytes[i] == '\"') {
                result.append("&quot;");
                continue;
            }
            if (utfBytes[i] == '\'') {
                result.append("&apos;");
                continue;
            }
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() == 2 && utfBytes[i] > 127) {
                result.append("&#x").append("00").append(hexB).append(";");
            } else if (hexB.length() > 2) {
                result.append("&#x").append(hexB).append(";");
            } else {
                result.append(utfBytes[i]);
            }
        }
        if (result.length() == 0) {
            result.append(" ");
        }
        return result.toString();
    }

    /**
     * 特殊字符的转义，例如：&#XD;表示回车
     *
     * @param s
     * @return
     */
    public static String characterDecode(String s) {
        String t;
        Character ch;
        int tmpPos, i;

        int maxPos = s.length();
        StringBuilder sb = new StringBuilder(maxPos);
        int curPos = 0;
        while (curPos < maxPos) {
            char c = s.charAt(curPos++);
            if (c == '&') {
                tmpPos = curPos;
                if (tmpPos < maxPos) {
                    char d = s.charAt(tmpPos++);
                    if (d == '#') {
                        if (tmpPos < maxPos) {
                            d = s.charAt(tmpPos++);
                            if ((d == 'x') || (d == 'X')) {
                                if (tmpPos < maxPos) {
                                    d = s.charAt(tmpPos++);
                                    if (isHexDigit(d)) {
                                        while (tmpPos < maxPos) {
                                            d = s.charAt(tmpPos++);
                                            if (!isHexDigit(d)) {
                                                if (d == ';') {
                                                    t = s.substring(curPos + 2, tmpPos - 1);
                                                    try {
                                                        i = Integer.parseInt(t, 16);
                                                        if ((i >= 0) && (i < 65536)) {
                                                            c = (char) i;
                                                            curPos = tmpPos;
                                                        }
                                                    } catch (NumberFormatException e) {
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else if (isDigit(d)) {
                                while (tmpPos < maxPos) {
                                    d = s.charAt(tmpPos++);
                                    if (!isDigit(d)) {
                                        if (d == ';') {
                                            t = s.substring(curPos + 1, tmpPos - 1);
                                            try {
                                                i = Integer.parseInt(t);
                                                if ((i >= 0) && (i < 65536)) {
                                                    c = (char) i;
                                                    curPos = tmpPos;
                                                }
                                            } catch (NumberFormatException e) {
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (isLetter(d)) {
                        while (tmpPos < maxPos) {
                            d = s.charAt(tmpPos++);
                            if (!isLetterOrDigit(d)) {
                                if (d == ';') {
                                    t = s.substring(curPos, tmpPos - 1);
                                    ch = (Character) charTable.get(t);
                                    if (ch != null) {
                                        c = ch.charValue();
                                        curPos = tmpPos;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static final HashMap<String, Character> charTable;

    static {
        // 定义特殊字符
        charTable = new HashMap<String, Character>();
        charTable.put("quot", Character.valueOf((char) 34));
        charTable.put("amp", Character.valueOf((char) 38));
        charTable.put("apos", Character.valueOf((char) 39));
        charTable.put("lt", Character.valueOf((char) 60));
        charTable.put("gt", Character.valueOf((char) 62));
        charTable.put("nbsp", Character.valueOf((char) 160));
        charTable.put("iexcl", Character.valueOf((char) 161));
        charTable.put("cent", Character.valueOf((char) 162));
        charTable.put("pound", Character.valueOf((char) 163));
        charTable.put("curren", Character.valueOf((char) 164));
        charTable.put("yen", Character.valueOf((char) 165));
        charTable.put("brvbar", Character.valueOf((char) 166));
        charTable.put("sect", Character.valueOf((char) 167));
        charTable.put("uml", Character.valueOf((char) 168));
        charTable.put("copy", Character.valueOf((char) 169));
        charTable.put("ordf", Character.valueOf((char) 170));
        charTable.put("laquo", Character.valueOf((char) 171));
        charTable.put("not", Character.valueOf((char) 172));
        charTable.put("shy", Character.valueOf((char) 173));
        charTable.put("reg", Character.valueOf((char) 174));
        charTable.put("macr", Character.valueOf((char) 175));
        charTable.put("deg", Character.valueOf((char) 176));
        charTable.put("plusmn", Character.valueOf((char) 177));
        charTable.put("sup2", Character.valueOf((char) 178));
        charTable.put("sup3", Character.valueOf((char) 179));
        charTable.put("acute", Character.valueOf((char) 180));
        charTable.put("micro", Character.valueOf((char) 181));
        charTable.put("para", Character.valueOf((char) 182));
        charTable.put("middot", Character.valueOf((char) 183));
        charTable.put("cedil", Character.valueOf((char) 184));
        charTable.put("sup1", Character.valueOf((char) 185));
        charTable.put("ordm", Character.valueOf((char) 186));
        charTable.put("raquo", Character.valueOf((char) 187));
        charTable.put("frac14", Character.valueOf((char) 188));
        charTable.put("frac12", Character.valueOf((char) 189));
        charTable.put("frac34", Character.valueOf((char) 190));
        charTable.put("iquest", Character.valueOf((char) 191));
        charTable.put("Agrave", Character.valueOf((char) 192));
        charTable.put("Aacute", Character.valueOf((char) 193));
        charTable.put("Acirc", Character.valueOf((char) 194));
        charTable.put("Atilde", Character.valueOf((char) 195));
        charTable.put("Auml", Character.valueOf((char) 196));
        charTable.put("Aring", Character.valueOf((char) 197));
        charTable.put("AElig", Character.valueOf((char) 198));
        charTable.put("Ccedil", Character.valueOf((char) 199));
        charTable.put("Egrave", Character.valueOf((char) 200));
        charTable.put("Eacute", Character.valueOf((char) 201));
        charTable.put("Ecirc", Character.valueOf((char) 202));
        charTable.put("Euml", Character.valueOf((char) 203));
        charTable.put("Igrave", Character.valueOf((char) 204));
        charTable.put("Iacute", Character.valueOf((char) 205));
        charTable.put("Icirc", Character.valueOf((char) 206));
        charTable.put("Iuml", Character.valueOf((char) 207));
        charTable.put("ETH", Character.valueOf((char) 208));
        charTable.put("Ntilde", Character.valueOf((char) 209));
        charTable.put("Ograve", Character.valueOf((char) 210));
        charTable.put("Oacute", Character.valueOf((char) 211));
        charTable.put("Ocirc", Character.valueOf((char) 212));
        charTable.put("Otilde", Character.valueOf((char) 213));
        charTable.put("Ouml", Character.valueOf((char) 214));
        charTable.put("times", Character.valueOf((char) 215));
        charTable.put("Oslash", Character.valueOf((char) 216));
        charTable.put("Ugrave", Character.valueOf((char) 217));
        charTable.put("Uacute", Character.valueOf((char) 218));
        charTable.put("Ucirc", Character.valueOf((char) 219));
        charTable.put("Uuml", Character.valueOf((char) 220));
        charTable.put("Yacute", Character.valueOf((char) 221));
        charTable.put("THORN", Character.valueOf((char) 222));
        charTable.put("szlig", Character.valueOf((char) 223));
        charTable.put("agrave", Character.valueOf((char) 224));
        charTable.put("aacute", Character.valueOf((char) 225));
        charTable.put("acirc", Character.valueOf((char) 226));
        charTable.put("atilde", Character.valueOf((char) 227));
        charTable.put("auml", Character.valueOf((char) 228));
        charTable.put("aring", Character.valueOf((char) 229));
        charTable.put("aelig", Character.valueOf((char) 230));
        charTable.put("ccedil", Character.valueOf((char) 231));
        charTable.put("egrave", Character.valueOf((char) 232));
        charTable.put("eacute", Character.valueOf((char) 233));
        charTable.put("ecirc", Character.valueOf((char) 234));
        charTable.put("euml", Character.valueOf((char) 235));
        charTable.put("igrave", Character.valueOf((char) 236));
        charTable.put("iacute", Character.valueOf((char) 237));
        charTable.put("icirc", Character.valueOf((char) 238));
        charTable.put("iuml", Character.valueOf((char) 239));
        charTable.put("eth", Character.valueOf((char) 240));
        charTable.put("ntilde", Character.valueOf((char) 241));
        charTable.put("ograve", Character.valueOf((char) 242));
        charTable.put("oacute", Character.valueOf((char) 243));
        charTable.put("ocirc", Character.valueOf((char) 244));
        charTable.put("otilde", Character.valueOf((char) 245));
        charTable.put("ouml", Character.valueOf((char) 246));
        charTable.put("divide", Character.valueOf((char) 247));
        charTable.put("oslash", Character.valueOf((char) 248));
        charTable.put("ugrave", Character.valueOf((char) 249));
        charTable.put("uacute", Character.valueOf((char) 250));
        charTable.put("ucirc", Character.valueOf((char) 251));
        charTable.put("uuml", Character.valueOf((char) 252));
        charTable.put("yacute", Character.valueOf((char) 253));
        charTable.put("thorn", Character.valueOf((char) 254));
        charTable.put("yuml", Character.valueOf((char) 255));
        charTable.put("OElig", Character.valueOf((char) 338));
        charTable.put("oelig", Character.valueOf((char) 339));
        charTable.put("Scaron", Character.valueOf((char) 352));
        charTable.put("scaron", Character.valueOf((char) 353));
        charTable.put("fnof", Character.valueOf((char) 402));
        charTable.put("circ", Character.valueOf((char) 710));
        charTable.put("tilde", Character.valueOf((char) 732));
        charTable.put("Alpha", Character.valueOf((char) 913));
        charTable.put("Beta", Character.valueOf((char) 914));
        charTable.put("Gamma", Character.valueOf((char) 915));
        charTable.put("Delta", Character.valueOf((char) 916));
        charTable.put("Epsilon", Character.valueOf((char) 917));
        charTable.put("Zeta", Character.valueOf((char) 918));
        charTable.put("Eta", Character.valueOf((char) 919));
        charTable.put("Theta", Character.valueOf((char) 920));
        charTable.put("Iota", Character.valueOf((char) 921));
        charTable.put("Kappa", Character.valueOf((char) 922));
        charTable.put("Lambda", Character.valueOf((char) 923));
        charTable.put("Mu", Character.valueOf((char) 924));
        charTable.put("Nu", Character.valueOf((char) 925));
        charTable.put("Xi", Character.valueOf((char) 926));
        charTable.put("Omicron", Character.valueOf((char) 927));
        charTable.put("Pi", Character.valueOf((char) 928));
        charTable.put("Rho", Character.valueOf((char) 929));
        charTable.put("Sigma", Character.valueOf((char) 931));
        charTable.put("Tau", Character.valueOf((char) 932));
        charTable.put("Upsilon", Character.valueOf((char) 933));
        charTable.put("Phi", Character.valueOf((char) 934));
        charTable.put("Chi", Character.valueOf((char) 935));
        charTable.put("Psi", Character.valueOf((char) 936));
        charTable.put("Omega", Character.valueOf((char) 937));
        charTable.put("alpha", Character.valueOf((char) 945));
        charTable.put("beta", Character.valueOf((char) 946));
        charTable.put("gamma", Character.valueOf((char) 947));
        charTable.put("delta", Character.valueOf((char) 948));
        charTable.put("epsilon", Character.valueOf((char) 949));
        charTable.put("zeta", Character.valueOf((char) 950));
        charTable.put("eta", Character.valueOf((char) 951));
        charTable.put("theta", Character.valueOf((char) 952));
        charTable.put("iota", Character.valueOf((char) 953));
        charTable.put("kappa", Character.valueOf((char) 954));
        charTable.put("lambda", Character.valueOf((char) 955));
        charTable.put("mu", Character.valueOf((char) 956));
        charTable.put("nu", Character.valueOf((char) 957));
        charTable.put("xi", Character.valueOf((char) 958));
        charTable.put("omicron", Character.valueOf((char) 959));
        charTable.put("pi", Character.valueOf((char) 960));
        charTable.put("rho", Character.valueOf((char) 961));
        charTable.put("sigmaf", Character.valueOf((char) 962));
        charTable.put("sigma", Character.valueOf((char) 963));
        charTable.put("tau", Character.valueOf((char) 964));
        charTable.put("upsilon", Character.valueOf((char) 965));
        charTable.put("phi", Character.valueOf((char) 966));
        charTable.put("chi", Character.valueOf((char) 967));
        charTable.put("psi", Character.valueOf((char) 968));
        charTable.put("omega", Character.valueOf((char) 969));
        charTable.put("thetasym", Character.valueOf((char) 977));
        charTable.put("upsih", Character.valueOf((char) 978));
        charTable.put("piv", Character.valueOf((char) 982));
        charTable.put("ensp", Character.valueOf((char) 8194));
        charTable.put("emsp", Character.valueOf((char) 8195));
        charTable.put("thinsp", Character.valueOf((char) 8201));
        charTable.put("zwnj", Character.valueOf((char) 8204));
        charTable.put("zwj", Character.valueOf((char) 8205));
        charTable.put("lrm", Character.valueOf((char) 8206));
        charTable.put("rlm", Character.valueOf((char) 8207));
        charTable.put("ndash", Character.valueOf((char) 8211));
        charTable.put("mdash", Character.valueOf((char) 8212));
        charTable.put("lsquo", Character.valueOf((char) 8216));
        charTable.put("rsquo", Character.valueOf((char) 8217));
        charTable.put("sbquo", Character.valueOf((char) 8218));
        charTable.put("ldquo", Character.valueOf((char) 8220));
        charTable.put("rdquo", Character.valueOf((char) 8221));
        charTable.put("bdquo", Character.valueOf((char) 8222));
        charTable.put("dagger", Character.valueOf((char) 8224));
        charTable.put("Dagger", Character.valueOf((char) 8225));
        charTable.put("bull", Character.valueOf((char) 8226));
        charTable.put("hellip", Character.valueOf((char) 8230));
        charTable.put("permil", Character.valueOf((char) 8240));
        charTable.put("prime", Character.valueOf((char) 8242));
        charTable.put("Prime", Character.valueOf((char) 8243));
        charTable.put("lsaquo", Character.valueOf((char) 8249));
        charTable.put("rsaquo", Character.valueOf((char) 8250));
        charTable.put("oline", Character.valueOf((char) 8254));
        charTable.put("frasl", Character.valueOf((char) 8260));
        charTable.put("euro", Character.valueOf((char) 8364));
        charTable.put("image", Character.valueOf((char) 8465));
        charTable.put("weierp", Character.valueOf((char) 8472));
        charTable.put("real", Character.valueOf((char) 8476));
        charTable.put("trade", Character.valueOf((char) 8482));
        charTable.put("alefsym", Character.valueOf((char) 8501));
        charTable.put("larr", Character.valueOf((char) 8592));
        charTable.put("uarr", Character.valueOf((char) 8593));
        charTable.put("rarr", Character.valueOf((char) 8594));
        charTable.put("darr", Character.valueOf((char) 8595));
        charTable.put("harr", Character.valueOf((char) 8596));
        charTable.put("crarr", Character.valueOf((char) 8629));
        charTable.put("lArr", Character.valueOf((char) 8656));
        charTable.put("uArr", Character.valueOf((char) 8657));
        charTable.put("rArr", Character.valueOf((char) 8658));
        charTable.put("dArr", Character.valueOf((char) 8659));
        charTable.put("hArr", Character.valueOf((char) 8660));
        charTable.put("forall", Character.valueOf((char) 8704));
        charTable.put("part", Character.valueOf((char) 8706));
        charTable.put("exist", Character.valueOf((char) 8707));
        charTable.put("empty", Character.valueOf((char) 8709));
        charTable.put("nabla", Character.valueOf((char) 8711));
        charTable.put("isin", Character.valueOf((char) 8712));
        charTable.put("notin", Character.valueOf((char) 8713));
        charTable.put("ni", Character.valueOf((char) 8715));
        charTable.put("prod", Character.valueOf((char) 8719));
        charTable.put("sum", Character.valueOf((char) 8721));
        charTable.put("minus", Character.valueOf((char) 8722));
        charTable.put("lowast", Character.valueOf((char) 8727));
        charTable.put("radic", Character.valueOf((char) 8730));
        charTable.put("prop", Character.valueOf((char) 8733));
        charTable.put("infin", Character.valueOf((char) 8734));
        charTable.put("ang", Character.valueOf((char) 8736));
        charTable.put("and", Character.valueOf((char) 8743));
        charTable.put("or", Character.valueOf((char) 8744));
        charTable.put("cap", Character.valueOf((char) 8745));
        charTable.put("cup", Character.valueOf((char) 8746));
        charTable.put("int", Character.valueOf((char) 8747));
        charTable.put("there4", Character.valueOf((char) 8756));
        charTable.put("sim", Character.valueOf((char) 8764));
        charTable.put("cong", Character.valueOf((char) 8773));
        charTable.put("asymp", Character.valueOf((char) 8776));
        charTable.put("ne", Character.valueOf((char) 8800));
        charTable.put("equiv", Character.valueOf((char) 8801));
        charTable.put("le", Character.valueOf((char) 8804));
        charTable.put("ge", Character.valueOf((char) 8805));
        charTable.put("sub", Character.valueOf((char) 8834));
        charTable.put("sup", Character.valueOf((char) 8835));
        charTable.put("nsub", Character.valueOf((char) 8836));
        charTable.put("sube", Character.valueOf((char) 8838));
        charTable.put("supe", Character.valueOf((char) 8839));
        charTable.put("oplus", Character.valueOf((char) 8853));
        charTable.put("otimes", Character.valueOf((char) 8855));
        charTable.put("perp", Character.valueOf((char) 8869));
        charTable.put("sdot", Character.valueOf((char) 8901));
        charTable.put("lceil", Character.valueOf((char) 8968));
        charTable.put("rceil", Character.valueOf((char) 8969));
        charTable.put("lfloor", Character.valueOf((char) 8970));
        charTable.put("rfloor", Character.valueOf((char) 8971));
        charTable.put("lang", Character.valueOf((char) 9001));
        charTable.put("rang", Character.valueOf((char) 9002));
        charTable.put("loz", Character.valueOf((char) 9674));
        charTable.put("spades", Character.valueOf((char) 9824));
        charTable.put("clubs", Character.valueOf((char) 9827));
        charTable.put("hearts", Character.valueOf((char) 9829));
        charTable.put("diams", Character.valueOf((char) 9830));
    }

    private static boolean isLetterOrDigit(char c) {
        return isLetter(c) || isDigit(c);
    }

    private static boolean isHexDigit(char c) {
        return isHexLetter(c) || isDigit(c);
    }

    private static boolean isLetter(char c) {
        return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
    }

    private static boolean isHexLetter(char c) {
        return ((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F'));
    }

    private static boolean isDigit(char c) {
        return (c >= '0') && (c <= '9');
    }

    // HTML is very particular about what constitutes white space.
    public static boolean isWhitespace(char ch) {
        return (ch == '\u0020') || (ch == '\r') || (ch == '\n') || (ch == '\u0009') || (ch == '\u000c')
                || (ch == '\u200b');
    }

    /**
     * 替换掉文件名中的怪异字符集
     *
     * @param fileName
     * @return
     */
    public static String validFileName(String fileName) {
        if (fileName == null)
            return "";
        /**
         * ?@#$&()\|;'"<>+-/
         */
        fileName = fileName.replace("?", "");
        fileName = fileName.replace("@", "");
        fileName = fileName.replace("#", "");
        fileName = fileName.replace("$", "");
        fileName = fileName.replace("&", "");
        fileName = fileName.replace("(", "");
        fileName = fileName.replace(")", "");
        fileName = fileName.replace("|", "");
        fileName = fileName.replace(";", "");
        fileName = fileName.replace("'", "");
        fileName = fileName.replace("\"", "");
        fileName = fileName.replace("<", "");
        fileName = fileName.replace(">", "");
        fileName = fileName.replace("+", "");
        fileName = fileName.replace("-", "");
        fileName = fileName.replace("/", "");
        fileName = fileName.replace("\\", "");
        fileName = fileName.replace("..", "");
        return fileName;
    }

    /**
     * 获取正在播放歌曲的url中的pid
     *
     * @param url
     * @return
     */
    public static String getPid(String url) {
        String pid = url;
        int len = pid.indexOf("PID=");
        if (len > 0) {
            pid = pid.substring(len + 4);
            int endLen = pid.indexOf("&");
            if (endLen > 0) {
                pid = pid.substring(0, endLen);
            }
        } else {
            pid = " ";
        }
        return pid;
    }

    /**
     * 替换掉字符串中选项 如:"mp3:amr:aac"中去掉"amr"
     *
     * @param target 被替换的字符串
     * @param src    原字符串
     * @param sep    分割符
     * @param type   1转换小写，2转换大写
     * @return 结果字符串
     */
    public static String removeItemFromStr(String target, String src, String sep, int type) {
        if (src != null && target != null && sep != null) {
            if (type == 1) {
                src = src.toLowerCase();
                target = target.toLowerCase();
            } else if (type == 2) {
                src = src.toUpperCase();
                target = target.toUpperCase();
            } else {
                // not ignore cases
            }
            if (target.equals(src)) {
                return "";
            } else {
                int idx = src.indexOf(sep.concat(target).concat(sep));
                if (idx != -1) {
                    src = src.replace(sep.concat(target).concat(sep), sep);
                } else {
                    if (src.startsWith(target.concat(sep))) {
                        src = src.substring(target.length() + sep.length());
                    } else if (src.endsWith(sep.concat(target))) {
                        src = src.substring(0, src.length() - (target.length() + sep.length()));
                    }
                }
            }
        }
        return src;
    }

    /**
     * 整数按除数得小数点的商 如：1234 按1024 转多少 K,保留小数点2位. formatIntToFloat(1234,1024,2)
     *
     * @param src     原整数
     * @param mode    除数
     * @param decimal 小数点位数
     * @return the float
     */
    public static float formatIntToFloat(int src, int mode, int decimal) {
        float ret = src * 1.0f;
        ret = ret / mode;
        BigDecimal b = new BigDecimal(ret);
        ret = b.setScale(decimal, BigDecimal.ROUND_HALF_UP).floatValue();
        return ret;
    }

    public static ArrayList<String> splitTextViewText(String txt, Paint paint, int lines, int... maxWidths) {

        ArrayList<String> list = new ArrayList<String>();
        if (isEmpty(txt)) {
            return list;
        }
        int istart = 0;
        char ch;
        int w = 0;
        boolean hasAppended = false;
        int lineCount = 0;
        int len = txt.length();
        int widthLen = maxWidths.length;
        StringBuilder[] sb = new StringBuilder[lines];
        for (int i = 0; i < len; i++) {
            ch = txt.charAt(i);
            float[] widths = new float[1];
            String srt = String.valueOf(ch);
            paint.getTextWidths(srt, widths);

            if (ch == '\n' || ch == '\r') {
                lineCount++;
                // list.add(txt.substring(istart, i));
                istart = i + 1;
                w = 0;
            } else {
                w += (int) (Math.ceil(widths[0]));
                if (w > maxWidths[lineCount < widthLen ? lineCount : 0]) {
                    lineCount++;
                    // list.add(txt.substring(istart, i));
                    istart = i;
                    i--;
                    w = 0;
                } else {
                    if (sb[lineCount] == null) {
                        sb[lineCount] = new StringBuilder();
                    }
                    sb[lineCount].append(ch);

                    if (i == (len - 1)) {
                        lineCount++;
                        // list.add(txt.substring(istart, len));
                    }
                }
            }
            if (lineCount == lines) {
                if (i < (len - 1)) {
                    hasAppended = true;
                }
                break;
            }
        }
        istart = 0;
        while (true) {
            if (sb[istart] != null) {
                list.add(sb[istart].toString());
            }
            istart++;
            if (istart >= lines)
                break;
        }
        int size = list.size();
        if (size == 0) {
            return list;
        }
        String last = list.get(size - 1);
        int l = last.length();
        if (size < lines) {
            list.add("...");
        } else {
            if (hasAppended && l > 3) {
                last = last.substring(0, l - 3).concat("...");
                list.set(size - 1, last);
            }
        }
        return list;
    }

    /**
     * 从字符串中过滤11位的手机号码 去掉+86神马的
     */
    public static String filterPhoneNumber(String phn) {
        if (isEmpty(phn)) {
            return "";
        }
        String p_num = phn;
        if (p_num.length() >= 11)// 大于等于11位
        {
            p_num = StringUtil.replace(phn, "+86", "");// 电话号码去掉+86 如果有的话
            p_num = p_num.replace("+", "");// 电话号码去掉+ 如果有的话
            if (p_num.length() > 11)
                p_num = p_num.substring(p_num.length() - 10);// 号码大于11位 就只取后面的11个数
        } else
        // 小于11位 将就用吧 有几位是几位
        {

        }
        return p_num;
    }

    /**
     * 自动分割文本
     *
     * @param content 需要分割的文本
     * @param p       画笔，用来根据字体测量文本的宽度
     * @param width   最大的可显示像素（一般为控件的宽度）
     * @return 一个字符串数组，保存每行的文本
     */
    public static String[] autoSplit(String content, Paint p, float width) {
        int length = content.length();
        float textWidth = p.measureText(content);
        if (textWidth <= width) {
            return new String[]{content};
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / width); // 计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {
            if (p.measureText(content, start, end) > width) { // 文本宽度超出控件宽度时
                lineTexts[i++] = (String) content.subSequence(start, end);
                start = end;
            }
            if (end == length) { // 不足一行的文本
                lineTexts[i] = (String) content.subSequence(start, end);
                break;
            }
            end += 1;
        }
        return lineTexts;
    }

    public static int getLineCount(String content, Paint p, float width) {
        int count = 0;
        String[] strs = autoSplit(content, p, width);
        count = strs.length;
        if (strs.length == 1) {
            int length = content.length();
            for (int i = 0; i < length; i++) {
                char ch = content.charAt(i);
                if (ch == '\n' || ch == '\r') {
                    ++count;
                }
            }
        }
        return count;
    }

    /**
     * 字符串从头开始 截取出限定长度的部分 中文长度 最多X个汉字的长度。。。
     */
    public static String getSubStringByCNlength(String in, int CNmaxlength) {
        if (StringUtil.isEmpty(in)) {
            return "";
        }
        int cnLength = 0;
        int enLength = 0;
        String a;
        StringBuffer retbuff = new StringBuffer();
        for (int i = 0; i < in.length(); i++) {
            // String a = in.substring(i, i + 1);
            a = String.valueOf(in.charAt(i));
            byte[] b = a.getBytes();
            if (b.length < 2) {
                enLength++;
            } else {
                cnLength++;
            }

            int Length = cnLength + (enLength + 1) / 2;
            if (Length == CNmaxlength) {
                return retbuff.append(a).toString();
            } else if (Length > CNmaxlength) {
                return retbuff.delete(retbuff.length() - 1, retbuff.length()).toString();
            }
            retbuff.append(a);
        }
        return in;
    }

    public static byte[] getBytes(String str) {
        return getBytes(str, null);
    }

    public static byte[] getBytes(String str, String charsetName) {
        byte[] ret = null;
        if (isEmpty(str))
            return ret;
        try {
            if (!isEmpty(charsetName)) {
                ret = str.getBytes(charsetName);
            } else {
                ret = str.getBytes("utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            ret = str.getBytes();
        }
        return ret;
    }

    /**
     * 字节转对应对应存储单位 最大到TB 最小KB
     */
    public static String getUnitBySize(long num) {
        String ret = "";
        try {
            DecimalFormat df = new DecimalFormat("0.##");
            if (num >= 1099511627776L)// TB
            {
                ret = df.format((float) num / 1099511627776L).concat("TB");
            } else if (num >= 1073741824)// GB
            {
                ret = df.format((float) num / 1073741824).concat("GB");
            } else if (num >= 1048576)// MB
            {
                ret = df.format((float) num / 1048576).concat("MB");
            } else
            /* if(num >= 1024) */// KB
            {
                ret = df.format((float) num / 1024).concat("KB");
            }
            // else //B
            // {
            // ret =""+num+"B";
            // }
        } catch (Exception e) {
        }
        return ret;
    }

    /**
     * 字符串折行
     */
    public static String[] splitStringLines(String str) {
        if (StringUtil.isEmpty(str))
            return null;
        Vector<String> strlines = new Vector<String>();
        int offset = 0;
        int pos = 0;
        while (offset < str.length()) {
            if (offset >= str.length() - 1) {
                if (str.substring(pos, offset).trim().length() > 1) {
                    strlines.addElement(str.substring(pos, str.length()).trim());
                }
                break;
            }
            if (str.charAt(offset) == '\n') {
                if (str.substring(pos, offset).trim().length() > 1) {
                    strlines.addElement(str.substring(pos, offset).trim());
                }
                pos = offset + 1;
            }
            offset++;
        }
        if (strlines.size() > 0) {
            String[] lines = strlines.toArray(new String[strlines.size()]);
            return lines;
        }
        return null;
    }

    public static boolean isEqual(String paramString1, String paramString2) {
        if ((isEmpty(paramString2)) || (isEmpty(paramString1))) {
            return false;
        }

        return paramString1.equals(paramString2);
    }

    //把日期转为字符串
    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return df.format(date);
    }

    public static Date converToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }
}
