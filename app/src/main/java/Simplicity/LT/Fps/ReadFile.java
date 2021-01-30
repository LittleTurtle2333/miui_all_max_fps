package Simplicity.LT.Fps;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFile {
	
    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	
    public ReadFile(String path) throws IOException {
        String s = "";
        DataOutputStream os = null;
		BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("cat " + path + "\n");
            os.writeBytes("exit\n");
            os.flush();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                s += line.trim() + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
            if (br != null) {
                br.close();
            }
        }
		pattern(s);	
    }
	

    private void pattern(String s) {
		//if (android.os.Build.VERSION.SDK_INT >= 26) {
			Pattern pattern = Pattern.compile("<Network>\\n([\\s\\S]+?)\\n\\</Network>");
			Matcher matcher = pattern.matcher(s);
			while (matcher.find()) {
				addOreo(matcher.group(), matcher.start(), matcher.end());
			}
		//} else {
			Pattern pattern1 = Pattern.compile("network=\\{\\n([\\s\\S]+?)\\n\\}");
			Matcher matcher1 = pattern1.matcher(s);
			while (matcher1.find()) {
				add(matcher1.group(), matcher1.start(), matcher1.end());
			}
		//}
    }

    private void add(String content, int s, int e) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("view", content);
		map.put("pos",s + "," + e);
        content = content.substring(9, content.length() - 2);
        String[] list = content.split("\\n");
        String x, y;
        for (String info : list) {
            int index = info.indexOf("=");
            if (index > -1) {
                x = info.substring(0, index);
                y = info.substring(index + 1);
            } else {
                continue;
            }
            if ("ssid".equals(x)) {
                if (y.charAt(0) == '"') {
                    y = y.substring(1, y.length() - 1);
                } else {
                    y = toUTF8(y);
                }
            } else if ("psk".equals(x)) {
                y = y.substring(1, y.length() - 1);
            }
            if (y == null) {
                continue;
            }
            map.put(x, y);
        }
        this.list.add(map);
    }
	
	
	private void addOreo(String content, int s, int e) {
        HashMap<String, String> map = new HashMap<String, String>();
		map.put("pos",s + "," + e);
        String[] list = content.split("\\n");
        String x;
        for (String info : list) {
			if (info.contains("name=\"SSID\">")) {
				x = getSubUtilSimple(info, "name=\"SSID\">&quot;(.*?)&quot;");
				map.put("ssid",x);
            } else if (info.contains("name=\"PreSharedKey\"")) {
				if (!info.contains("null")) {
					x = getSubUtilSimple(info, "name=\"PreSharedKey\">&quot;(.*?)&quot;");
					map.put("psk", x);
				}
            }
        }
		map.put("view", content);
        this.list.add(map);
    }
	
    public ArrayList<Map<String, String>> getList(Context context) {
        ArrayList<Map<String, String>> m = new ArrayList<Map<String, String>>();
        for (Map<String, String> map : this.list) {
			if (map.containsKey("ssid") && map.containsKey("psk")) {
                m.add(map);
            }
        }
		return this.sorting(m, context);
    }

    private ArrayList<Map<String, String>> sorting(ArrayList<Map<String, String>> lv, Context context) {
        if (lv.size() < 1) {
            return lv;
        }

        Collections.sort(lv, new sort(getCurrentSSID(context)));

        return lv;
    }

    private String getCurrentSSID(Context context) {
        try {
            WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (!mWifi.isWifiEnabled()) {
                return "";
            }
            WifiInfo wifiInfo = mWifi.getConnectionInfo();
            String ssid = wifiInfo.getSSID();

            if (ssid.charAt(0) == '"') {
                ssid = ssid.substring(1, ssid.length() - 1);
            } else {
                ssid = toUTF8(ssid);
            }

            return ssid;
        } catch (Exception e) {
			
            return "";
        }
    }
	
    private static String toUTF8(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.toUpperCase();
        int total = s.length() / 2;
        int pos = 0;
        byte[] buffer = new byte[total];
        for (int i = 0; i < total; i++) {
            int start = i * 2;
            buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
            pos++;
        }
        return new String(buffer, 0, pos, StandardCharsets.UTF_8);
    }
	
	public static String getSubUtilSimple(String soap, String rgex){
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        while(m.find()){
            return m.group(1);
        }
        return "";
    }
	
}

class sort implements Comparator<Map<String, String>> {

    private String currentSSID;

    public sort(String currentSSID) {
        this.currentSSID = currentSSID;
    }

    @Override
    public int compare(Map<String, String> t1, Map<String, String> t2) {
        String s1 = t1.get("ssid");
        String s2 = t2.get("ssid");

        if (s1.equals(s2)) {
            return 0;
        }

        if (s1.equals(currentSSID)) {
            return -1;
        }
        if (s2.equals(currentSSID)) {
            return 1;
        }
        return s1.compareToIgnoreCase(s2);
    }
}

