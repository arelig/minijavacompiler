///[Error:a|11]
// Redefinición de a(...)->B retorna un tipo clase 

class A
{
	dynamic String a(int n, A m, B o){}
}

class B extends A
{
	dynamic Init a(int n, A m, B o){}
}

class Init
{
	static void main(int argc){}
}
