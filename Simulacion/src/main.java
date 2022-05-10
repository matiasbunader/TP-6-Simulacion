import java.util.ArrayList;
import java.util.Random;
import static java.lang.Math.*;

public class main {

    public static void main(String[] args) {

        //CONDICIONES INICIALES :
        int cantidadDeMaquinasModelo1 = 3;
        int cantidadDeMaquinasModelo2 = 2;
        
        int tiempo = 0;
        int tiempoFinal = 43200; // 1 Mes
        int tiempoProximaLlegada = 0;
        int tiempoLimiteDeRechazo; // 5 horas
        int condicionTiempoRechazo = 360;
        int intervaloEntreArribos = 0;
    	int menorTiempoComprometidoModelo1=0;
    	int menorTiempoComprometidoModelo2=0;
    	int menorTiempoComprometido=0;
    	int tiempoPromedioModelo2 = 15;

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

            if (validarHoraLaboral(tiempo) == 1) {
            	
            	if(obtenerDia(tiempo) == 1) {
            		
            		intervaloEntreArribos = intervaloDeArribo1();
            		
            	}
            	
            	else if(obtenerDia(tiempo) == 2) {
            		
            		intervaloEntreArribos = intervaloDeArribo2();
            		
            		
            	}
            	
            }
            
            tiempoProximaLlegada = tiempo + intervaloEntreArribos;
            

            int minimoTC1 = obtenerMinimoTC1(cantidadDeMaquinasModelo1,tiempoComprometidoPorMaquinaModelo1);
            int minimoTC2 = obtenerMinimoTC2(cantidadDeMaquinasModelo2,tiempoComprometidoPorMaquinaModelo2);
            
            menorTiempoComprometidoModelo1 = tiempoComprometidoPorMaquinaModelo1.get(minimoTC1);
            menorTiempoComprometidoModelo2 = tiempoComprometidoPorMaquinaModelo2.get(minimoTC2);
            
            if(menorTiempoComprometidoModelo2 <= menorTiempoComprometidoModelo1) {
            	//menorTiempoComprometido = menorTiempoComprometidoModelo2;
            //SE ELIGE MÁQUINA DE MODELO 2	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            	
            	pedidosTotales = pedidosTotales + 1;

                if(tiempo >= menorTiempoComprometidoModelo2){
                     //NO HAY ESPERA

                    int STOMaquinaModelo2 = sumatoriaTiempoOciosoPorMaquinaModelo2.get(minimoTC2);
                    STOMaquinaModelo2 = STOMaquinaModelo2 + (tiempo - tiempoComprometidoPorMaquinaModelo2.get(minimoTC2));
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
            else {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            	pedidosTotales = pedidosTotales + 1;

                if(tiempo >= menorTiempoComprometidoModelo1){
                     //NO HAY ESPERA

                    int STOMaquinaModelo1 = sumatoriaTiempoOciosoPorMaquinaModelo1.get(minimoTC1);
                    STOMaquinaModelo1 = STOMaquinaModelo1 + (tiempo - tiempoComprometidoPorMaquinaModelo1.get(minimoTC1));
                    sumatoriaTiempoOciosoPorMaquinaModelo1.add(minimoTC1, STOMaquinaModelo1);

                    tiempoComprometidoPorMaquinaModelo1.add(minimoTC1, tiempo + tiempoDeAtencion());

                    pedidosAceptados = pedidosAceptados + 1;

                }
                else{
                    //HAY ESPERA
                    tiempoLimiteDeRechazo = tiempo + condicionTiempoRechazo;

                    if(tiempoComprometidoPorMaquinaModelo1.get(minimoTC1) < tiempoLimiteDeRechazo){
                        //SE ACEPTA EL PEDIDO

                        sumatoriaTiempoEspera = sumatoriaTiempoEspera + (tiempoComprometidoPorMaquinaModelo1.get(minimoTC1) - tiempo);

                        tiempoComprometidoPorMaquinaModelo1.add(minimoTC1,tiempoComprometidoPorMaquinaModelo1.get(minimoTC1) + tiempoDeAtencion());

                        pedidosAceptados = pedidosAceptados + 1;
                    }

                    else{
                        // SE RECHAZA EL PEDIDO

                        pedidosRechazados = pedidosRechazados + 1;
                    }
                }

            	
              }
            
            





        }
        // SE TERMINO LA SIMULACION, IMPRIMO RESULTADOS

        double promedioTiempoDeEspera = sumatoriaTiempoEspera / pedidosAceptados;

        ArrayList<Integer> porcentajeTiempoOciosoModelo1 = new ArrayList<Integer>();
        ArrayList<Integer> porcentajeTiempoOciosoModelo2 = new ArrayList<Integer>();
        

        for(int i = 0 ; i< cantidadDeMaquinasModelo1 ; i++)
        {
            porcentajeTiempoOciosoModelo1.add(i,(sumatoriaTiempoOciosoPorMaquinaModelo1.get(i) * 100 / tiempo)) ;
        }
        
        for(int i = 0 ; i< cantidadDeMaquinasModelo2 ; i++)
        {
            porcentajeTiempoOciosoModelo2.add(i,(sumatoriaTiempoOciosoPorMaquinaModelo2.get(i) * 100 / tiempo)) ;
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
            System.out.println("MAQUINA DE MODELO 1" + (i + 1) +" :" + porcentajeTiempoOciosoModelo1.get(i) + " %" );
        }

        System.out.println("PORCENTAJE TIEMPO OCIOSO DE CADA MAQUINA DE MODELO 2:");
        for(int i = 0 ; i< cantidadDeMaquinasModelo2 ; i++)
        {
            System.out.println("MAQUINA DE MODELO 2" + (i + 1) +" :" + porcentajeTiempoOciosoModelo2.get(i) + " %" );
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
    		
    		if ((tiempo%10080) >= 4320 && (tiempo%10080 <=7200)) {
    			
    			dia = 2;
    		}
    		
    		else {
    			
    			dia = 3;
    		}
    		
    	}
    
    	
    	return dia;
    	
    }
    
    
    static int intervaloDeArribo1(){
        Random random = new Random();
        double numeroRandom = random.nextInt(100) * 0.01;
        return (int) (Math.log(-numeroRandom+1)/(-0.0004));	
       // return (int) (numeroRandom * (300 - 180) + 180);
    }
    
    static int intervaloDeArribo2(){
        Random random = new Random();
        double numeroRandom = random.nextInt(100) * 0.01;
        return (int) (Math.log(-numeroRandom+1)/(-0.0002));
       // return (int) (numeroRandom * (300 - 180) + 180);
    }

    static int tiempoDeAtencion(){
        Random random = new Random();
        double numeroRandom = random.nextInt(100) * 0.01;

        return (int) (((2)/(Math.pow((1/numeroRandom-1),(1/1310.5)))) + 1310.5);
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

