package screenshot;

public class Wraper
{
    int x;
    int xHasta;
    int yDesde;
    int yHasta;

    public Wraper()
    {
    }

    public Wraper(int x, int xHasta, int yDesde, int yHasta)
    {
        this.x = x;
        this.xHasta = xHasta;
        this.yDesde = yDesde;
        this.yHasta = yHasta;
    }

    

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getyDesde()
    {
        return yDesde;
    }

    public void setyDesde(int yDesde)
    {
        this.yDesde = yDesde;
    }

    public int getyHasta()
    {
        return yHasta;
    }

    public void setyHasta(int yHasta)
    {
        this.yHasta = yHasta;
    }

    public int getxHasta()
    {
        return xHasta;
    }

    public void setxHasta(int xHasta)
    {
        this.xHasta = xHasta;
    }

    @Override
    public String toString()
    {
        return "(" + "x=" + x + ", xHasta=" + xHasta + ") dx = " + getDiferencialX() + " | (yDesde=" + yDesde + ", yHasta=" + yHasta + ") dy = "+ getDiferencialY() +"";
    }
    
    public boolean esConsecutivo(Wraper wrap2)
    {
        int masMenos = 2;
        boolean esConsecutivo = false;
        
        if(this.getyDesde() == wrap2.getyDesde())
        {
            if(this.getyHasta() == wrap2.getyHasta())
            {
                int margenSuperior = wrap2.getxHasta()+ masMenos;
                int margenInferior = wrap2.getxHasta()- masMenos;
                if(this.getxHasta()>= margenInferior && this.getxHasta() <= margenSuperior  )
                {
                    esConsecutivo = true;
                }
            }
        }
        return esConsecutivo;
    }
    public int calcularArea()
    {
        int diferencialX = xHasta - x;
        int diferencialY = yHasta - yDesde;
        
        return (diferencialX * diferencialY);
    }
    
    public int getDiferencialX()
    {
        int diferencialX = xHasta - x;
        return diferencialX;
    }
    public int getDiferencialY()
    {
        int diferencialY = yHasta - yDesde;
        return diferencialY;
    }
}
