import java.util.*;
import java.io.*;
import java.lang.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class PasswdValidator{
 
  public static String error = "";
  public static String warning = "";
  public static HashMap<String,Integer> username_hmap = new HashMap<String,Integer>();
  public static HashMap<String,Integer> uid_hmap = new HashMap<String,Integer>();
  
  public static void main(String[] args) throws Exception{
    ArrayList<String> lines = new ArrayList<String>();
    Scanner sc = new Scanner(new File("pt.txt"));
    while(sc.hasNextLine()){
      String line = sc.nextLine();
      lines.add(line);
    }
    for(int i = 0;i < lines.size();i++){
      error = "";
      warning = "";
      String line = lines.get(i);
      if(line.charAt(0) != '#'){
        String[] info = line.split(":");
        int ch = 0;
        if (eval_username(info[0], i) == 1 && ch == 0) ch = 1;
        if(eval_password(info[1], i) == 1 && ch == 0) ch = 1;
        if(eval_uid(info[2], i) == 1 && ch == 0) ch = 1;
        if(eval_gid(info[3], i) == 1 && ch == 0) ch = 1;
        if(eval_gecos(info[4], i) == 1 && ch == 0) ch = 1;
        if(eval_directory(info[5], i) == 1 && ch == 0) ch = 1;
        if(eval_shell(info[6], i) == 1 && ch == 0) ch = 1;
      }
      if (!error.equals("")) System.out.println(error);
      else if(warning != "") System.out.println(warning);
    }
  }
  
  public static int eval_username(String str, int i){
    if(str.length() > 8){
      set_error("- username Field size greater than 8",i);
      return 1;
    }else if(Character.isLetter(str.charAt(0)) == false){
      set_error("- username First character is not a letter", i);
      return 1;
    }else{
      String goodChars = "[a-zA-z0-9._-]*";
      Pattern good = Pattern.compile(goodChars);
      Matcher g = good.matcher(str);
      if(g.matches() == false){
        set_error("- username Contains invalid character",i);
        return 1;
      }
      for(int z = 0; z<str.length();z++){
        if((str.charAt(z) == '.') || (Character.isUpperCase(str.charAt(z)))){
          set_warning("- username Contains dot or uppercase letter",i);
        }
      }
      if(username_hmap.containsKey(str)){
        set_error("-username Username already exists",i);
        return 1;
      }else{
        username_hmap.put(str,1);
      }
    }
    return 0;
  }
 
public static int eval_password(String str, int i){
    if (str.length() != 1 || str.charAt(0) != 'x' && str.charAt(0) != '*'){
      set_error("- password must be x or *", i);
      return 1;
    }
    return 0;
  }
 
  public static int eval_uid(String str, int i){
    if (str.length() == 0) {
      set_error("- uid shouldn't be empty", i);
      return 1;
    }
    int uid = -1;
    try {
      uid = Integer.parseInt(str);
    } catch (NumberFormatException e){
      set_error("- uid must be a non-negative integer", i);
    }
    if (uid < 0) {
      set_error("- uid must be a non-negative integer", i);
      return 1;
    }
    if (str.charAt(0) == '0' && str.length() > 1) {
      set_error("- uid must have no leading 0s", i);
      return 1;
    }
    if(uid_hmap.containsKey(str)){
      set_warning("- Uid already exists",i);
    }else{
      uid_hmap.put(str,1);
    }
    return 0;
  }
 
  public static int eval_gid(String str, int i){
    if (str.length() == 0) {
      set_error("- gid shouldn't be empty", i);
      return 1;
    }
    int gid = -1;
    try {
      gid = Integer.parseInt(str);
    } catch (NumberFormatException e){
      set_error("- gid must be a non-negative integer", i);
    }
    if (gid < 0) {
      set_error("- gid must be a non-negative integer", i);
      return 1;
    }
    if (str.charAt(0) == '0' && str.length() > 1) {
      set_error("- gid must have no leading 0s", i);
      return 1;
    }
    return 0;
  }
 
  public static int eval_gecos(String str, int i){
    if (str.length() == 0) {
      set_error("- gecos must have at least a name", i);
      return 1;
    }
    String [] info = str.split(",");
    if (info[0].length() == 0) {
      set_error("- gecos must have at least a name", i);
      return 1;
    }
    for (int a = 0; a < info[0].length() - 1; a++){
      if (info[0].charAt(a) == ' ' && info[0].charAt(a + 1) == ' '){
        set_error("- full name contains 2 adjacent spaces", i);
        return 1;
      }
    }
    Pattern r = Pattern.compile("[a-zA-z0-9'._-]{1}[a-zA-z0-9' ._-]*[a-zA-z0-9'._-]{1}");
    Matcher ra = r.matcher(info[0]);
    if(ra.matches() == false){
      set_error("- gecos full name contains invalid character",i);
      return 1;
    }
    
    if (info.length > 1){
      if (info[1].length() == 0){
        set_error("- gecos must non empty name for office", i);
        return 1;
      }else {
        String sp = "[a-zA-Z0-9]([a-zA-Z0-9]+[_ -.]?)*[a-zA-Z0-9]";
        Pattern good = Pattern.compile(sp);
        Matcher g = good.matcher(info[1]);
        if(g.matches() == false){
          set_error("- gecos office contains invalid character",i);
          return 1;
        }
      }
    }
    if (info.length > 2){
      if (info[2].length() == 0){
        set_error("- gecos must non empty phone number", i);
        return 1;
      }else {
        Pattern good = Pattern.compile("[+]?[0-9]{4}[0-9]*");
        Matcher g = good.matcher(info[2]);
        if(g.matches() == false){
          set_error("- gecos phone Contains invalid character",i);
          return 1;
        }
      }
    }
    if (info.length > 3){
      if (info[3].length() == 0){
        set_error("- the home phone must not be empty", i);
        return 1;
      } else {
        Pattern good = Pattern.compile("[+]?[0-9]{4}[0-9]*");
        Matcher g = good.matcher(info[3]);
        if(g.matches() == false){
          set_error("- home phone contains invalid character",i);
          return 1;
        }
      }
    }
    
    return 0;
  }
 
  public static int eval_directory(String str, int i){
    if((str.charAt(0) != '/') || (str.charAt(str.length()-1)) == '/'){
      set_error("DIRECTORY Directory must start with a / and not end with one",i);
      return 1;
    }
    for(int z = 0; z < str.length() - 1; z++){
      if(str.charAt(z) == '/'){
        if((str.charAt(z+1) == '-') || (str.charAt(z+1) == '/')){
          set_warning("DIRECTORY '/' cannot be followed by another '/' or a -", i);
        }
      }
    }
    String directoryPatternStr = "[/[a-zA-Z0-9._-]+]+";
    Pattern dirPattern = Pattern.compile(directoryPatternStr);
    Matcher m = dirPattern.matcher(str);
    if(m.matches() == false){
      set_error("DIRECTORY Invalid directory",i);
      return 1;
    }
    return 0;
  }
 
  public static int eval_shell(String str, int i){
    String stringA[] = {"/bin/bash", "/bin/csh", "/bin/ksh",
                "/bin/Sh", "/bin/tcsh", "/bin/zsh"};
     int check = 0;
    for (int z = 0; z < stringA.length; z++) {
        if (str.equals(stringA[z])) {
        check = 1;
        }
    }
    if (check == 0) {
        set_error("SHELL Invalid shell",i);
        return 1;
    }
    return 0;
  }
 
  public static void set_error(String reason, int i){
   error = i+": ERROR "+reason;
  }
 
  public static void set_warning(String reason, int i){
    warning = i+": WARNING "+reason;
  }
}
 
 
 
//  public static int strResult(String in){
//    in = in.trim();
//    String dnaPattern = ". [N|E|S|W|n|e|s|w]{4} .{4}";
//    String stepPattern = "\\d+";
//    String commentPattern = "[#]+.*";
//    String blankPattern = "[ \t\n]+";
//    Pattern blank = Pattern.compile(blankPattern);
//    Pattern comment = Pattern.compile(commentPattern);
//    Pattern step = Pattern.compile(stepPattern);
//    Pattern dna = Pattern.compile(dnaPattern);
//    Matcher a = blank.matcher(in);
//    Matcher b = comment.matcher(in);
//    Matcher c = step.matcher(in);
//    Matcher d = dna.matcher(in);
//    if(in.length() == 0){
//      return 4;
//    }else if (a.matches()){
//      return 4;
//    }else if (b.matches()){
//      return 3;
//    }else if (c.matches()){
//      return 2;
//    }else if (d.matches()){
//      return 1;
//    }else{
//      return 0;
//    }
//  }
//}