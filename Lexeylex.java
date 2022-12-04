import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexeylex{
	private Pattern numeric = Pattern.compile("-?\\d+(\\.\\d+)?");
	private Pattern var = Pattern.compile("[A-Za-z][A-Za-z][A-Za-z][A-Za-z][A-Za-z][A-Za-z][A-Za-z]?[A-Za-z]?");
	static int pointer;
	static ArrayList<String> parsing;
	

	
	public static ArrayList<String> findLexie(String code) {
		ArrayList<String> lexies = new ArrayList<String>();
		
		String arrOfCode[] = code.split(" ");
		
		for( String token : arrOfCode) {
			
			
			if(token.equals("begin")) {
				lexies.add("begin");
				
			}
			else if(token.equals("end")) {
				lexies.add("end");
			}
			else if(token.equals("watching")) {
				lexies.add("watching");
			}
			else if(token.equals("compare")) {
				lexies.add("switch");
			}
			else if(token.equals("event")) {
				lexies.add("case");
			}
			else if(token.equals("[1]") || token.equals("[2]") || token.equals("[4]") || token.equals("[8]")) {
				lexies.add("var_storage");
			}
			else if(token.equals("and")) {
				lexies.add("and");
			}
			else if(token.equals("or")) {
				lexies.add("or");
			}
			else if(token.equals(";")) {
				lexies.add(";");
			}
			else if(token.equals("+")) {
				lexies.add("+");
			}
			else if(token.equals("-")) {
				lexies.add("-");
			}
			else if(token.equals("*")) {
				lexies.add("*");
			}
			else if(token.equals("%")) {
				lexies.add("%");
			}
			else if(token.equals("=")) {
				lexies.add("=");
			}
			else if(token.equals("<")) {
				lexies.add("<");
			}
			else if(token.equals(">")) {
				lexies.add(">");
			}
			else if(token.equals("<=")) {
				lexies.add("<=");
			}
			else if(token.equals(">=")) {
				lexies.add(">=");
			}
			else if(token.equals("==")) {
				lexies.add("==");
			}
			else if(token.equals("!=")) {
				lexies.add("!=");
			}
			else if(token.equals( "(")) {
				lexies.add("(");
			}
			else if(token.equals(")")) {
				
				lexies.add(")");
			}
			else if(token.equals("{")) {
				lexies.add("{");
				
			}
			else if(token.equals("}")) {
				lexies.add("}");
			}
			else if(isNumeric(token)) {
				lexies.add("int_lit");
			}
			else if(isVar(token)) {
				lexies.add("variable");
			}
			else if(token.equals("")) {
				
			}
			else if(token.equals("\\n")) {
				
			}
			else {
				
				lexError(token);
				break;
			}
		}
	return lexies;
	}

	
	public static void lexError(String token) {
		
		System.out.println("lexical error oh nooooooo " + "{"+token+"}");
	}
	
	public static boolean isNumeric(String string) {
		try {
		    int intValue = Integer.parseInt(string);
		    return true;
		} catch (NumberFormatException e) {
		    
		}
		return false;
	
	}
	 public static boolean isVar(String string) {
		 boolean bool = Pattern.matches("[A-Za-z][A-Za-z][A-Za-z][A-Za-z][A-Za-z][A-Za-z][A-Za-z]?[A-Za-z]?", string);  
		 return bool;
	 }
	 
	 public static void startRDA(ArrayList<String> tokens) {
		 parsing = tokens;
		 pointer = 0;
		 String value = parsing.get(pointer);
		 //program starter -> “begin”
		 if(value.equals("begin")) {
			 getNextToken();
			 checkStatement();
		 	}else {syntaxError(currentToken());}
		
		 }
	 
	 
	 
	 public static String getNextToken() {
		 if(pointer<parsing.size()-1) {
			 ++pointer;
			 return parsing.get(pointer); 
			}
		 else return "end of code";
	 }
	 
	 public static String currentToken() {
		 return parsing.get(pointer);
	 }
	 
	 public static void checkStatement() {
		String current = currentToken(); 
		if(current.equals("variable")) {
			checkVar();
		}else if(current.equals("{")){
			checkBlock();
		}else if(current.equals("watching")) {
			checkWhile();
		}else if(current.equals("switch")) {
			checkSwitch();
		}else {syntaxError(current);}
	 }
	 
	 
	 public static void checkVar() {
		 if (currentToken().equals("variable")) {
			 getNextToken();
			 if(currentToken().equals("var_storage")) {
				 getNextToken();
			 }else if(currentToken().equals("=")) {
				 getNextToken();
				 checkExpression();
			 }else syntaxError(currentToken());
		 }
	 }
	 
	 
	 
	 public static void checkBlock() {
		 if(currentToken().equals("{")) {
			String current =  getNextToken();
			 if(current.equals("watching")|| current.equals("variable") || current.equals("switch")) {
				 checkStatement();
				 if(currentToken().equals(";")) {
					 getNextToken();
					 checkStatement();
				 } else syntaxError(currentToken());
				 if (currentToken().equals("}")) {
					 getNextToken();
				 } else syntaxError(currentToken());
			 }
		 }
	 }	
	 
	 public static void checkWhile() {
		if(currentToken().equals("watching")) {
			getNextToken();
			if( currentToken().equals("(")) {
				checkBool();
				if(currentToken().equals(")")) {
					getNextToken();
					checkBlock();
				} else { syntaxError(currentToken());}
			} else {syntaxError(currentToken());}
		}else {syntaxError(currentToken());} 
	 }
	 
	 public static void checkSwitch() {
		 if(currentToken().equals("switch")) {
			 getNextToken();
			 if(currentToken().equals("(")) {
				 getNextToken();
				 checkBool();
				 if(currentToken().equals(")")) {
					 getNextToken();
					 checkBlock();
					 if(currentToken().equals("case")) {
						 getNextToken();
						 checkBlock();
					 }else {syntaxError(currentToken());}
				 }else {syntaxError(currentToken());}
			 }else {syntaxError(currentToken());}
		 
		 
		 
		 }else {syntaxError(currentToken());}
		 
	 }
	 
	 public static void checkExpression() {
		 checkTerm();
		 if(currentToken().equals("*")||currentToken().equals("/")||currentToken().equals("%")) {
			 getNextToken();
			 checkTerm();
		 }
	 }
	 
	 public static void checkTerm() {
		 checkFactor();
		 if(currentToken().equals("+")||currentToken().equals("-")) {
			 getNextToken();
			 checkFactor();
		 }
	 }
	 
	 public static void checkFactor() {
		 if(currentToken().equals("variable")||currentToken().equals("int_lit")) {
			 getNextToken();
			 
		 }else if (currentToken().equals("(")){
			 getNextToken();
			 checkExpression();
			 if(currentToken().equals(")")) {
				 getNextToken();
			 } else syntaxError(currentToken());
		 } else syntaxError(currentToken());
	 }
	 
	 public static void checkBool() {
		 checkOr();
		 if (currentToken().equals("and")) {
			 getNextToken();
			 checkOr();
		 }else {syntaxError(currentToken());}
	 }
	
	 public static void checkOr() {
		checkEqual();
		if(currentToken().equals("or")) {
			getNextToken();
			checkEqual();
		}else {syntaxError(currentToken());}
	 }
	 
	 public static void checkEqual() {
		 checkBoolValue();
		 if(currentToken().equals("==")|| currentToken().equals("!=")) {
			 getNextToken();
			 checkBoolValue();
		 }else if(currentToken().equals("=")) {
			 syntaxError(currentToken());
		 }
	 }
	 
	public static void checkBoolValue() {
		checkBoolExp();
		if(currentToken().equals(">=") || currentToken().equals(">") || currentToken().equals("<=")
		|| currentToken().equals("<")) {
			getNextToken();
			checkBoolExp();
		} else {syntaxError(currentToken());}
			
	}
	
	public static void checkBoolExp() {
		checkBoolTerm();
		if(currentToken().equals("*")|| currentToken().equals("/")||currentToken().equals("%")) {
			getNextToken();
			checkBoolTerm();
		} else {syntaxError(currentToken());}
	}
	
	public static void checkBoolTerm() {
		checkBoolFactor();
		if (currentToken().equals("+")||currentToken().equals("-")) {
			getNextToken();
			checkBoolFactor();
		}else {syntaxError(currentToken());}
	}
	
	public static void checkBoolFactor() {
		if(currentToken().equals("variable")|| currentToken().equals("int_lit")) {
			getNextToken();
		}
		else if(currentToken().equals("(")) {
				checkBoolExp();
				if(currentToken().equals(")")) {
					getNextToken();
				}else syntaxError(currentToken());
			}else syntaxError(currentToken());
		}
	
	

	
	 public static void syntaxError(String token) {
		 System.out.println("oh noooo syntax error at: " +token + " {pointer: " + pointer+"}");
	 }
	 
	 
	 
	 public static void main (String[] args) throws IOException {
		
		String fileToText1 = "";
		
		//for test files, i had to add a space to end of all lines of "code" just to assist with split function and regex matching
		
		
	    Path path1 = Path.of("/Users/janisgrikstas/eclipse-workspace/PLCExam2/test1.txt");
	    String text1 = Files.readString(path1);
	    fileToText1 = text1.replace("\n", "").replace("\r", "");
		ArrayList<String> lexies1 = findLexie(fileToText1);
		String tokens_printed1 = lexies1.toString();
		System.out.println(tokens_printed1);
	    
		//reset pointers to 0
		//clear out arraylist parsing
		pointer = 0;
		startRDA(lexies1);
		
		
		
		String fileToText2 = "";
		
	    Path path2 = Path.of("/Users/janisgrikstas/eclipse-workspace/PLCExam2/test2.txt");
	    String text2 = Files.readString(path2);
	    fileToText2 = text2.replace("\n", "").replace("\r", "");
		ArrayList<String> lexies2 = findLexie(fileToText2);
		String tokens_printed2 = lexies2.toString();
		System.out.println(tokens_printed2);
	    
	    
	    parsing.clear();
	    pointer = 0;
	    startRDA(lexies2);
	    
	    
	    
	    String fileToText3 = "";
		
	    Path path3 = Path.of("/Users/janisgrikstas/eclipse-workspace/PLCExam2/test3.txt");
	    String text3 = Files.readString(path3);
	    fileToText3 = text3.replace("\n", "").replace("\r", "");
		ArrayList<String> lexies3 = findLexie(fileToText3);
		String tokens_printed3 = lexies3.toString();
		System.out.println(tokens_printed3);
	    
	    
	    parsing.clear();
	    pointer = 0;
	    startRDA(lexies3);
	    
	    String fileToText4 = "";
		
	    Path path4 = Path.of("/Users/janisgrikstas/eclipse-workspace/PLCExam2/test4.txt");
	    String text4 = Files.readString(path4);
	    fileToText4 = text4.replace("\n", "").replace("\r", "");
		ArrayList<String> lexies4 = findLexie(fileToText4);
		String tokens_printed4 = lexies4.toString();
		System.out.println(tokens_printed4);
	   
	    
	    parsing.clear();
	    pointer = 0;
	    startRDA(lexies4);
	 
	 }

}

