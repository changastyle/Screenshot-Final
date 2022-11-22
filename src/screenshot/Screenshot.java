package screenshot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Date;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import ws.JSONWS;

public class Screenshot
{
//    public static final String ipServer = "viewdevs.com.ar"; //:8080
    public static final boolean verbose = false;
    public static final String ipServer = "localhost:9000";
    public static final String fullURLWS = "http://" + ipServer + "/api/";
    public static final String rutaSO = "C:\\temp\\screenshots\\";
    public static void main(String[] args) throws AWTException, IOException, InterruptedException
    {
        int contador = 0;
        while(true)
        {
            String respuesta = JSONWS.sendData( false, fullURLWS + "remote/reportarme", "", "");
            
            if(respuesta != null && respuesta.trim().length() > 0 )
            {
                boolean pidioScreenshot = Boolean.valueOf(respuesta);
                
                if(pidioScreenshot)
                {
                    System.out.println("debo sacar foto");
                    System.out.println("SCREEN SIZE:" + new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                    BufferedImage image = new Robot().createScreenCapture(new Rectangle(1920,1080));
                    Date timestamp = new Date();
                    File foto = new File(rutaSO + File.separator + "screenshot-"+ contador+ ".jpg");
                    ImageIO.write(image, "jpeg",foto ); //+ timestamp.getTime() 

                    System.out.println("resultado de subir archivo: " + JSONWS.sendFile(fullURLWS + "remote/subirFoto", "foto", foto));

                    contador++;
                }
                else
                {
                    if(verbose)
                    {
                        System.out.println("no saco foto");
                    }
                }
            }
            
            
            //System.out.println("respuesta de reportarme: " + respuesta);
            
            
            Thread.sleep(1000);
        }   
        
    }
    public static BufferedImage getDifferenceImage(BufferedImage img1, BufferedImage img2) {
        // convert images to pixel arrays...
        final int w = img1.getWidth(),
                h = img1.getHeight(), 
                highlight = Color.MAGENTA.getRGB();
        final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
        final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);
        // compare img1 to img2, pixel by pixel. If different, highlight img1's pixel...
        for (int i = 0; i < p1.length; i++) {
            if (p1[i] != p2[i]) {
                p1[i] = highlight;
            }
        }
        // save img1's pixels to a new BufferedImage, and return it...
        // (May require TYPE_INT_ARGB)
        final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        out.setRGB(0, 0, w, h, p1, 0, w);
        return out;
    }
    public static int diferencia(BufferedImage img1, BufferedImage img2) throws IOException
    {
        int sumatoriaTodoLoQueCambio = 0;
        boolean sonDistintas = false;
        boolean sonIgualDeAnchas = false;
        boolean sonIgualDeAltas = false;
        
        int x1,x2;
        int y1,y2;
        
        x1 = img1.getWidth();
        x2 = img1.getWidth();
        y1 = img1.getHeight();
        y2 = img1.getHeight();
        
        //ANCHO:
        if(x1 > x2)
        {
            System.out.println("img 1 es mas ancha");
        }
        else if(x1 == x2)
        {
            //System.out.println("son igual de anchas");
            sonIgualDeAnchas = true;
        }
        else
        {
            System.out.println("img 2 es mas ancha");
        }
        
        //ALTO:
        if(y1 > y2)
        {
            System.out.println("img 1 es mas alta");
        }
        else if(y1 == y2)
        {
           // System.out.println("son igual de altas");
            sonIgualDeAltas = true;
        }
        else
        {
            System.out.println("img 2 es mas alta");
        }
        
        if(sonIgualDeAltas && sonIgualDeAnchas)
        {
            System.out.println("tienen el mismo tamaño (" + x1 + "," + y1 + ")");
            
            List<Wraper> wrapperList = new ArrayList<Wraper>();
            
            for(int x = 0 ; x < x1 ; x++)
            {
                for(int y = 0 ; y < y1 ; y++)
                {
                    if(img1.getRGB(x, y) != img2.getRGB(x, y))
                    {
                        int posUltimaX = posWrap(wrapperList, x);
                        if( posUltimaX != -1)
                        {
                            wrapperList.get(posUltimaX).setyHasta(y);
                        }
                        else
                        {
                            wrapperList.add( new Wraper(x,x, y, y));
                        }
                        //System.out.println("cambian en (" + x + "," +  y + ") = " + img1.getRGB(x, y) + " | " + img2.getRGB(x, y));
                    }
                }
            }
            
            List<Wraper> listadoFinal = mostrarListaCambios(wrapperList);
            sumatoriaTodoLoQueCambio = 0;
            int areaTotalDeLaImagen = (x1 * y1);
            for(Wraper wrap : listadoFinal)
            {
                int areaTotalQueCambio =  wrap.calcularArea();
                int porcentajeCambio = (areaTotalQueCambio * 100) /areaTotalDeLaImagen;
                sumatoriaTodoLoQueCambio += porcentajeCambio;
                
                BufferedImage image = img2;
                
                if( wrap.getDiferencialY() > 0 &&  wrap.getDiferencialX() > 0)
                {
                    System.out.println("cambio en "+ wrap.toString() + " | area = "+  wrap.calcularArea() + " (" + porcentajeCambio + "%)");
                    BufferedImage imagenCortada = cropImage(img1, wrap);
                    ImageIO.write(imagenCortada, "jpeg", new File("/screen/cambios/recorte-("+ wrap.getX()+"-"+ wrap.getxHasta()+"_"+ wrap.getxHasta() + "-" + wrap.getyDesde() + ").jpg"));
                    /*BufferedImage thumbnail = Thumbnails.of(img1).size(200, 200)
                    .asBufferedImage();
                    ImageIO.write(thumbnail, "jpeg", new File("/screen/cambios/recorte-("+ wrap.getX()+"-"+ wrap.getxHasta()+"_"+ wrap.getxHasta() + "-" + wrap.getyDesde() + ").jpg"));
                    *///BufferedImage imagenCortada = img2;
                    //imagenCortada.getScaledInstance(wrap.getDiferencialY() , wrap.getDiferencialX() , Image.SCALE_SMOOTH);
                    //ImageIO.write(imagenCortada, "jpeg", new File("/screen/cambios/screenshot-("+ wrap.getX()+"-"+ wrap.getxHasta()+"_"+ wrap.getxHasta() + "-" + wrap.getyDesde() + ").jpg"));
                }
                
                
                System.out.println("cambio en "+ wrap.toString() + " | area = "+  wrap.calcularArea() + " (" + porcentajeCambio + "%)");
            }
            //System.out.println("area de la imagen: " + areaTotalDeLaImagen + " pixeles  = (100%)" );
            System.out.println("area total que cambio (" + sumatoriaTodoLoQueCambio + "%)");
            
            
        }
        else
        {
            sonDistintas = true;
        }
        
        
        return sumatoriaTodoLoQueCambio;
    }
    public static BufferedImage cropImage(BufferedImage src, Wraper wraper) 
    {
       BufferedImage dest = src.getSubimage(wraper.x, wraper.yDesde,wraper.xHasta,wraper.yHasta);
       return dest; 
    }
    public static int posWrap(List<Wraper> lista, int xQueBusco)
    {
        int posRespuesta = -1;
        int contador = 0;
        for(Wraper wraper: lista)
        {
            if(wraper.getX() == xQueBusco)
            {
                posRespuesta = contador;
            }
            contador++;
        }
        return posRespuesta;
    }
    public static List<Wraper> mostrarListaCambios(List<Wraper> lista)
    {
        List<Wraper> listadoFinal = new ArrayList<Wraper>();
        Wraper ultimoDeLaLista = null;
        for(Wraper wrap : lista)
        {
            if(ultimoDeLaLista == null)
            {
                ultimoDeLaLista = wrap;
                listadoFinal.add(ultimoDeLaLista);
            }
            else
            {
                if(wrap.esConsecutivo(ultimoDeLaLista))
                {
                    ultimoDeLaLista.setxHasta(wrap.getX());
                    listadoFinal.set( (listadoFinal.size() - 1) , ultimoDeLaLista);
                }
                else
                {
                    ultimoDeLaLista = wrap;
                    listadoFinal.add(ultimoDeLaLista);
                }
            }
            
            
            //System.out.println("cambio en "+ wrap.getX()+ " desde y0 = " + wrap.getyDesde() + ", hasta yf = " + wrap.getyHasta());
        }
        
        //REALMENTE MUESTRA EL RESULTADO FINAL:
       /* for(Wraper wrap : listadoFinal)
        {
            System.out.println("cambio en "+ wrap.toString());
        }*/
        
        return listadoFinal;
    }
}
