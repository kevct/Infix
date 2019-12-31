/*	Kevin Tran
	Lab 10
	Class Section 01
	Lab Section 03 */
import java.io.*;
import java.util.*;

class infix{
	static LinkedList<Token> infixToPostfix(String s) throws Exception{
		Stack<Operator> theStack = new Stack<Operator>();
		theStack.push(new Operator('#'));
		
		LinkedList<Token> theQueue = new LinkedList<Token>();
		Tokenizer T = new Tokenizer(s);

		while(T.moreTokens()){
			Token tkn = T.nextToken();
			if (tkn instanceof Operand){ //if its a number then enqueue
				theQueue.addLast(tkn);				
			}else{
				Operator opr = (Operator)tkn;
				if(opr.operator =='('){ //if operator is '(' then push regardless of precedence
					theStack.push(opr);
				}else if(opr.operator ==')'){ //if operator is ')' then keep enqueueing the stack until you reach '('
					try{
						while((theStack.peek()).operator != '('){
							theQueue.addLast((Token)theStack.pop());
						}
						theStack.pop(); //Pops '(' out of the stack
					}catch(Exception e){
						throw new infixException(errorType.ExcessRightParenthesis); //too many )
					}
				}else{ //if not '(' or ')'
					if(opr.precedence() <= (theStack.peek()).precedence()){ //if precedence is lower or equal to the top of stack then enqueue the stack until a operator with lower precedence is reached
						while(opr.precedence() <= (theStack.peek()).precedence()){
							theQueue.addLast(theStack.pop());
						}
					}
					theStack.push(opr);
				}
			}
		}
		while((theStack.peek()).operator != '#'){ //once there are no more tokens enqueue whatever else is in stack until '#' is reached
			if((theStack.peek()).operator == '('){
				throw new infixException(errorType.ExcessLeftParenthesis); //too many (
			}else{
				theQueue.addLast((Token)theStack.pop());
			}
		}
		return theQueue;
	}
	static int evaluePostfix(LinkedList<Token> Post) throws Exception{
		Stack<Operand> theStack = new Stack<Operand>();
		
		Token tkn;
		
		while(!Post.isEmpty()){
			tkn = Post.removeFirst();
			if(tkn instanceof Operand){ //if token is a number then push into stack
				theStack.push((Operand)tkn);
			}else{//if token is a not a number then it must be an operator
				try{
					Operator opr = (Operator)tkn; //convert token to operator
					Operand opnd2 = theStack.pop(); //pop the top number in stack and typecast to Operand
					Operand opnd1 = theStack.pop(); //pop the top number in stack and typecast to Operand
					Operand result = new Operand(0); //create a new operand and set it to temp value of 0
					switch(opr.operator){ //whatever the operator is do that function and set result equal to the result
						case '+':
							result.operand = opnd1.operand + opnd2.operand;
							break;
						case '-':
							result.operand = opnd1.operand - opnd2.operand;
							break;
						case '*':
							result.operand = opnd1.operand * opnd2.operand;
							break;
						default:
							result.operand = opnd1.operand / opnd2.operand;
							break;
					}
					theStack.push(result); //push result into the stack
				}catch(Exception e){
					throw new infixException(errorType.ExcessOperator); //too many operator
				}
			}
		}
		int top = (theStack.pop()).operand;
		try{
			if(!theStack.empty()){
				throw new infixException(errorType.ExcessOperand); //too many operand
			}
		}catch(Exception e){
			throw new infixException(errorType.ExcessOperand);
		}
		return top; //since its assumed the infix operation is correct then there should be only one number at the end of the postfix operation and so pops that one number out
	}

	public static void main(String[] args) throws IOException {
		LinkedList<Token> Post;
		while(true) {
			System.out.print("Enter infix: ");
			System.out.flush();
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String s = br.readLine();
			if ( s.equals("") ) break;
			try{
				Post = infixToPostfix(s);
				System.out.print("Postfix is ");
				for(Token st : Post){
					System.out.print(st + " ");
				}
				System.out.println();
				int result = evaluePostfix(Post);
				System.out.print("Result is " + result);
				System.out.println();
			}catch(Exception e){
				System.out.println(e);
			}
		}
    }
}
enum errorType { ExcessLeftParenthesis, ExcessRightParenthesis, ExcessOperator, ExcessOperand};
class infixException extends Exception {
	private errorType etype;
	public infixException(errorType et) { // constructor
		etype = et;
	}
	public String toString() {
		return "***** " + etype.name() + " *****";
	}
}
