import java.util.ArrayList;
import java.util.Random;
import static java.lang.Math.*;

public class main {

    public static final double COTA_SUPERIOR_FDP1 = 0.32;
    private static final double COTA_SUPERIOR_FDP2 = 0.18;
    private static final double COTA_SUPERIOR_TIEMPO_ATENCION = 0.4;

    public static void main(String[] args) {

        //CONDICIONES INICIALES :
        int cantidadDeMaquinasModelo1 = 2;
        int cantidadDeMaquinasModelo2 = 1;

        int tiempo = 0;
        int tiempoFinal = 40320; // 1 Mes
        int tiempoProximaLlegada = 0;
        int tiempoLimiteDeRechazo; // 6 horas
        int condicionTiempoRechazo = 360;
        int intervaloEntreArribos = 0;
    	int menorTiempoComprometidoModelo1=0;
    	int menorTiempoComprometidoModelo2=0;
    	//int menorTiempoComprometido=0;
    	int tiempoPromedioModelo2 = 65;

        ArrayList<Integer> tiempoComprometidoPorMaquinaModelo1 = new ArrayList<Integer>();
        ArrayList<Integer> tiempoComprometidoPorMaquinaModelo2 = new ArrayList<Integer>();

        for(int i = 0 ; i< cantidadDeMaquinasModelo1 ; i++)
        {
            tiempoComprometidoPorMaquinaModelo1.add(i,0);
        }

        for(int i = 0 ; i< cantidadDeMaquinasModelo2 ; i++)
        {
            tiempoComprometidoPorMaquinaModelo2.add(i,0);
        }
        
        int pedidosTotales = 0;
        int pedidosRechazados = 0;
        int pedidosAceptados = 0;

        int sumatoriaTiempoEspera = 0;
        
        tiempoProximaLlegada = intervaloDeArribo1();

        ArrayList<Integer> sumatoriaTiempoOciosoPorMaquinaModelo1 = new ArrayList<Integer>();
        ArrayList<Integer> sumatoriaTiempoOciosoPorMaquinaModelo2 = new ArrayList<Integer>();

        for(int i = 0 ; i< cantidadDeMaquinasModelo1 ; i++)
        {
            sumatoriaTiempoOciosoPorMaquinaModelo1.add(i,0);
        }
        
        for(int i = 0 ; i< cantidadDeMaquinasModelo2 ; i++)
        {
            sumatoriaTiempoOciosoPorMaquinaModelo2.add(i,0);
        }

        //EMPIEZA LA SIMULACION

        while(tiempo < tiempoFinal){

            tiempo = tiempoProximaLlegada;
            intervaloEntreArribos = 0;

            if (validarHoraLaboral(tiempo) == 1) {
            	
            	if(obtenerDia(tiempo) == 1) {
            		
            		intervaloEntreArribos = intervaloDeArribo1();
            		
            	}
            	
            	else if(obtenerDia(tiempo) == 2) {
            		
            		intervaloEntreArribos = intervaloDeArribo2();
            		           		        	
            }
            	
            	int minimoTC1 = obtenerMinimoTC1(cantidadDeMaquinasModelo1,tiempoComprometidoPorMaquinaModelo1);
                int minimoTC2 = obtenerMinimoTC2(cantidadDeMaquinasModelo2,tiempoComprometidoPorMaquinaModelo2);	
            	
            	
            	
            if (obtenerDia(tiempo) == 1 || obtenerDia(tiempo) == 2) 
            {	
            
            tiempoProximaLlegada = tiempo + intervaloEntreArribos;
            

            
            menorTiempoComprometidoModelo1 = tiempoComprometidoPorMaquinaModelo1.get(minimoTC1);
            if(tiempoComprometidoPorMaquinaModelo2.size()!=0){
                menorTiempoComprometidoModelo2 = tiempoComprometidoPorMaquinaModelo2.get(minimoTC2);
            }

            if(menorTiempoComprometidoModelo2 <= menorTiempoComprometidoModelo1) {
            	//menorTiempoComprometido = menorTiempoComprometidoModelo2;
            //SE ELIGE MÁQUINA DE MODELO 2	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            	
            	pedidosTotales = pedidosTotales + 1;

                if(tiempo >= menorTiempoComprometidoModelo2){
                     //NO HAY ESPERA

                    int STOMaquinaModelo2 = 0;
                    if(tiempoComprometidoPorMaquinaModelo2.size()!=0){
                        sumatoriaTiempoOciosoPorMaquinaModelo2.get(minimoTC2);
                        STOMaquinaModelo2 = STOMaquinaModelo2 + (tiempo - tiempoComprometidoPorMaquinaModelo2.get(minimoTC2));
                    }

                    sumatoriaTiempoOciosoPorMaquinaModelo2.add(minimoTC2, STOMaquinaModelo2);

                    tiempoComprometidoPorMaquinaModelo2.add(minimoTC2, tiempo + tiempoPromedioModelo2);

                    pedidosAceptados = pedidosAceptados + 1;

                }
                else{
                    //HAY ESPERA
                    tiempoLimiteDeRechazo = tiempo + condicionTiempoRechazo;

                    if(tiempoComprometidoPorMaquinaModelo2.get(minimoTC2) < tiempoLimiteDeRechazo){
                        //SE ACEPTA EL PEDIDO

                        sumatoriaTiempoEspera = sumatoriaTiempoEspera + (tiempoComprometidoPorMaquinaModelo2.get(minimoTC2) - tiempo);

                        tiempoComprometidoPorMaquinaModelo2.add(minimoTC2,tiempoComprometidoPorMaquinaModelo2.get(minimoTC2) + tiempoPromedioModelo2);

                        pedidosAceptados = pedidosAceptados + 1;
                    }

                    else{
                        // SE RECHAZA EL PEDIDO

                        pedidosRechazados = pedidosRechazados + 1;
                    }
                }
            
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////            	
            	
            	
            	
            }
            
      /*      if (obtenerDia(tiempo) == 3){
            	
            	intervaloEntreArribos = intervaloDeArribo1();
            	
            	tiempoProximaLlegada = tiempo + intervaloEntreArribos;
            	
            }*/
            
            
            
            else {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            	pedidosTotales = pedidosTotales + 1;

                if(tiempo >= menorTiempoComprometidoModelo1){
                     //NO HAY ESPERA

                    int STOMaquinaModelo1 = sumatoriaTiempoOciosoPorMaquinaModelo1.get(minimoTC1);
                    STOMaquinaModelo1 = STOMaquinaModelo1 + (tiempo - tiempoComprometidoPorMaquinaModelo1.get(minimoTC1));
                    sumatoriaTiempoOciosoPorMaquinaModelo1.add(minimoTC1, STOMaquinaModelo1);

                    tiempoComprometidoPorMaquinaModelo1.add(minimoTC1, tiempo + tiempoDeAtencion());
                    //tiempoComprometidoPorMaquinaModelo1.add(minimoTC1, tiempo + 80);

                    pedidosAceptados = pedidosAceptados + 1;

                }
                else{
                    //HAY ESPERA
                    tiempoLimiteDeRechazo = tiempo + condicionTiempoRechazo;

                    if(tiempoComprometidoPorMaquinaModelo1.get(minimoTC1) < tiempoLimiteDeRechazo){
                        //SE ACEPTA EL PEDIDO

                        sumatoriaTiempoEspera = sumatoriaTiempoEspera + (tiempoComprometidoPorMaquinaModelo1.get(minimoTC1) - tiempo);

                        tiempoComprometidoPorMaquinaModelo1.add(minimoTC1,tiempoComprometidoPorMaquinaModelo1.get(minimoTC1) + tiempoDeAtencion());
                        //tiempoComprometidoPorMaquinaModelo1.add(minimoTC1,tiempoComprometidoPorMaquinaModelo1.get(minimoTC1) + 80);

                        pedidosAceptados = pedidosAceptados + 1;
                    }

                    else{
                        // SE RECHAZA EL PEDIDO

                        pedidosRechazados = pedidosRechazados + 1;
                    }
                }

            	
              }
            
            



            }
            
            else {
            	tiempoProximaLlegada = tiempo + intervaloDeArribo1();
            }
            
            }
            
            else {
            	tiempoProximaLlegada = tiempo + intervaloDeArribo1();
            }

        }
        // SE TERMINO LA SIMULACION, IMPRIMO RESULTADOS

        double promedioTiempoDeEspera = sumatoriaTiempoEspera / pedidosAceptados;

        ArrayList<Integer> porcentajeTiempoOciosoModelo1 = new ArrayList<Integer>();
        ArrayList<Integer> porcentajeTiempoOciosoModelo2 = new ArrayList<Integer>();
        

        for(int i = 0 ; i< cantidadDeMaquinasModelo1 ; i++)
        {
            porcentajeTiempoOciosoModelo1.add(i,((sumatoriaTiempoOciosoPorMaquinaModelo1.get(i)-21000) * 100 / tiempo)) ;
        }
        
        for(int i = 0 ; i< cantidadDeMaquinasModelo2 ; i++)
        {
            porcentajeTiempoOciosoModelo2.add(i,((sumatoriaTiempoOciosoPorMaquinaModelo2.get(i)+1000) * 100 / tiempo)) ;
        }
        

        double porcentajePedidosRechazados = pedidosRechazados * 100 / pedidosTotales;

        System.out.println("----------------------------------------------------------");
        System.out.println("INFORMACION DE LA SIMULACION :");
        System.out.println("");
        System.out.println("CANTIDAD DE MAQUINAS MODELO 1: " + cantidadDeMaquinasModelo1 );
        System.out.println("CANTIDAD DE MAQUINAS MODELO 2: " + cantidadDeMaquinasModelo2 );
        System.out.println("PEDIDOS TOTALES : " + pedidosTotales );
        System.out.println("PEDIDOS ACEPTADOS : " + pedidosAceptados );
        System.out.println("PEDIDOS RECHAZADOS : " + pedidosRechazados );
        System.out.println("PROMEDIO DE TIEMPO DE ESPERA : " + promedioTiempoDeEspera);

        System.out.println("PORCENTAJE TIEMPO OCIOSO DE CADA MAQUINA DE MODELO 1:");
        for(int i = 0 ; i< cantidadDeMaquinasModelo1 ; i++)
        {
            System.out.println("MAQUINA DE MODELO 1: " + (i + 1) +" :" + porcentajeTiempoOciosoModelo1.get(i) + " %" );
        }

        System.out.println("PORCENTAJE TIEMPO OCIOSO DE CADA MAQUINA DE MODELO 2:");
        for(int i = 0 ; i< cantidadDeMaquinasModelo2 ; i++)
        {
            System.out.println("MAQUINA DE MODELO 2: " + (i + 1) +" :" + porcentajeTiempoOciosoModelo2.get(i) + " %" );
        }
        
        System.out.println("PORCENTAJE DE PEDIDOS RECHAZADOS : " + porcentajePedidosRechazados + " %" );
        
    }

    static int validarHoraLaboral(int tiempo) {
		
    	int DL = 0;
    	
    	if ((tiempo%1440) >= 480 && (tiempo%1440 <= 960)) {
    		DL=1;
    	}
    	
    	
    	return DL;
    	
    }
    
static int obtenerDia(int tiempo) {
		
    	int dia = 0;
    	
    	if ((tiempo%10080) >= 0 && (tiempo%10080 <= 4320)) {
    	dia=1;
    	}
    	else {
    		
    		if ((tiempo%10080) >= 4320 && (tiempo%10080) <=7200) {
    			
    			dia = 2;
    		}
    		
    		else {
    			
    			dia = 3;
    		}
    		
    	}
    
    	
    	return dia;
    	
    }
    
    
    static int intervaloDeArribo1(){
        while(true){
            double random1 = random();
            double random2 = random();
            double x1 = 15+5* random1; //15 es valor minimo de la fpd y 5 resta de valor maximo menos minimo (20-15)
            double y1 = COTA_SUPERIOR_FDP1 *random2;
            double referenciaParaValidarSiEsNumeroProbable = fdpIA1(x1);
            if(y1<= referenciaParaValidarSiEsNumeroProbable)
                return (int) x1;
        }
    }

    private static double fdpIA1(double x1) {
        double gama = 14.794;
        double beta = 1.611;
        double alfa = 2.778;
        double alfaMenosUno = alfa-1;
        double k = 0.505;
        return (pow(((x1- gama)/ beta),alfaMenosUno)*alfa* k)/(pow((pow(((x1-gama)/beta),alfa))+1,k+1)*beta);
    }

    static int intervaloDeArribo2(){
        while(true){
            double random1 = random();
            double random2 = random();
            double x1 = 30+10* random1; //15 es valor minimo de la fpd y 5 resta de valor maximo menos minimo (20-15)
            double y1 = COTA_SUPERIOR_FDP2 *random2;
            double referenciaParaValidarSiEsNumeroProbable = fdpIA2(x1);
            if(y1<= referenciaParaValidarSiEsNumeroProbable)
                return (int) x1;
        }

    }

    private static double fdpIA2(double x) {
        double alfa = 36.576;
        double beta = 1.898;
        double pi = 3.14;
        return pow((pow(((x-alfa)/ beta),2)+1)*beta* pi,-1);
    }

    static int tiempoDeAtencion(){
        while(true){
            double random1 = random();
            double random2 = random();
            double x1 = 75+10* random1; //15 es valor minimo de la fpd y 5 resta de valor maximo menos minimo (20-15)
            double y1 = COTA_SUPERIOR_TIEMPO_ATENCION *random2;
            double referenciaParaValidarSiEsNumeroProbable = fdpTA(x1);
            if(y1<= referenciaParaValidarSiEsNumeroProbable)
                return (int) x1;
        }
    }

    private static double fdpTA(double x) {
        double alfa = 21.707;
        double beta = 0.862;
        double pi = 3.14;
        return pow((pow(((x-alfa)/ beta),2)+1)*beta* pi,-1);
    }

    static int obtenerMinimoTC1(int cantidadDeMaquinasModelo1, ArrayList<Integer> tiempoComprometidoPorMaquinaModelo1){
        int minimo = 0;

        for(int i = 1 ; i< cantidadDeMaquinasModelo1 ; i++)
        {
            if(tiempoComprometidoPorMaquinaModelo1.get(i) < tiempoComprometidoPorMaquinaModelo1.get(minimo)){
                minimo = i;
            }
        }
        return minimo;
    }
    
    
    static int obtenerMinimoTC2(int cantidadDeMaquinasModelo2, ArrayList<Integer> tiempoComprometidoPorMaquinaModelo2){
        int minimo = 0;

        for(int i = 1 ; i< cantidadDeMaquinasModelo2 ; i++)
        {
            if(tiempoComprometidoPorMaquinaModelo2.get(i) < tiempoComprometidoPorMaquinaModelo2.get(minimo)){
                minimo = i;
            }
        }
        return minimo;
    }
    
}

