/*	Kevin Tran
	Lab 10
	Class Section 01
	Lab Section 03 */
class Tokenizer {
	private char [] Buf;
	private int cur;
	Tokenizer(String infixExpression) {
		Buf = infixExpression.toCharArray();
		cur = 0;
	}
	boolean moreTokens() {
		//Skip blanks.
		while(cur<Buf.length && Buf[cur] == ' '){
			cur++;
		}
		return cur<Buf.length;
	}
	Token nextToken() {
		/*
		1. Skip blanks.
		2. if (cur>=Buf.length) return null;
		3. If the next character is a digit, keep reading until a non-digit is read.
		Convert the string of digits into an integer.
		*/
		while(cur<Buf.length && Buf[cur] == ' '){
			cur++;
		}
		
		if(cur >= Buf.length){
			return null;
		}else if(cur<Buf.length && Buf[cur] >= '0' && Buf[cur] <= '9'){
			String Digits;
			int start = cur;
			int len = 0;
			while(cur<Buf.length && Buf[cur] >= '0' && Buf[cur] <= '9'){
				len++;
				cur++;
			}
			Digits = new String(Buf, start, len);
			int num = Integer.valueOf(Digits).intValue();
			return new Operand(num);
		}else{
			return new Operator(Buf[cur++]);
		}
		/*
		 Use num to create and return an operand.
		4. Otherwise, use the next character to create and return an operator.
		*/
	}
}