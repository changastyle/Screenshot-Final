package screenshot;
public class BrechaFila
{
   private Wraper inicial;
   private Wraper ultimo;

    public BrechaFila()
    {
    }

    public BrechaFila(Wraper inicial, Wraper ultimo)
    {
        this.inicial = inicial;
        this.ultimo = ultimo;
    }

    public Wraper getInicial()
    {
        return inicial;
    }

    public void setInicial(Wraper inicial)
    {
        this.inicial = inicial;
    }

    public Wraper getUltimo()
    {
        return ultimo;
    }

    public void setUltimo(Wraper ultimo)
    {
        this.ultimo = ultimo;
    }
   
   
   
}
