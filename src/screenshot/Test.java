package screenshot;

import static screenshot.Screenshot.fullURLWS;
import ws.JSONWS;

public class Test
{
    public static final boolean verbose = false;
    public static final String ipServer = "localhost:9000";
    public static final String fullURLWS = "http://" + ipServer + "/api/";
    public static final String rutaSO = "C:\\temp\\screenshots\\";
    
    public static void main(String args[])
    {
        String respuesta = JSONWS.sendData( false, fullURLWS + "remote/reportarme", "", "");
        
        System.out.println("RESPUESTA DE REPORTARME:" + respuesta);
    }
    
}
