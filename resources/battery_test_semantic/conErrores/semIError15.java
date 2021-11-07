///[Error:a|11]
// Redefinión errónea: no puede hacerse un método estático dinámicos

class A
{
	static int a(int n, A m, B o){}
}

class B extends A
{
	dynamic int a(int n, A m, B o){}
}
