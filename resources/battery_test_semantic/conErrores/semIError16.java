///[Error:a|11]
// Redefinión errónea: no coinciden los tipos

class A
{
	dynamic int a(int c, A m, B o){}
}

class B extends A
{
	static int a(int c, A m, B o){}
}

class Init
{
	static void main(){}
}
