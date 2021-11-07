///[Error:b|7]
// Métodos de mismo nombre y tipo, pero con diferente tipo de retorno.

class A
{
	dynamic int b(){}
	static A b(){}
}
