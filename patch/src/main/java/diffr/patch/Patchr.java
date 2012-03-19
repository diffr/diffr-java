package diffr.patch;

import java.io.*;
import java.util.ArrayList;

public class Patchr {
	private ArrayList<String> orig;
	private ArrayList<String> patch;
	private String origfile;
	private String patchfile;
	
	public Patchr(String origfile, String patchfile) {
		this.orig = new ArrayList<String>();
		this.patch = new ArrayList<String>();
		this.origfile = origfile;
		this.patchfile = patchfile;
	}

	public void patch() {
		populate();
		try {
			for (String pline : patch) {
				if (pline.startsWith("> ")) {
					System.out.println(pline.substring(2));
				} else {
					String[] s = pline.split(",");
					int start = Integer.parseInt(s[0]);
					int end = Integer.parseInt(s[1]);
					for (int i = start ; i <= end ; i++) {
						System.out.println(orig.get(i-1));
					}
				}
			}
		} catch (Exception e) {}
	}
	
	private void populate() {
		try {
			FileInputStream fstream = new FileInputStream(this.origfile);
			 
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			  
			while ((strLine = br.readLine()) != null)   {
				this.orig.add(strLine);
			}
			
			in.close();
		} catch (Exception e){ 
			System.err.println("Could not read file " + this.origfile);
			System.exit(-2);
		}
		
		try {
			FileInputStream fstream = new FileInputStream(this.patchfile);
			 
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			  
			while ((strLine = br.readLine()) != null)   {
				this.patch.add(strLine);
			}
			
			in.close();
		} catch (Exception e){ 
			System.err.println("Could not read file " + this.patchfile);
			System.exit(-2);
		}
		
	}
}
