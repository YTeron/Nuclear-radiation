package net.yteron.nucrad.radiation;

import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RadiationCupp {
    private static double cupp = 0.0;
    private static double maxcupp = 10000.0;
    private static double min = 0.0;
    public static double getcupp(){return cupp;}
    public static void addcup(double raditional){if(cupp>10000) cupp+=raditional;}
    public static void minuscup(double raditional){if(cupp<0) cupp-=raditional;}

    @SubscribeEvent
    public static void ManagersCup(){
        if(cupp>500)
            //тошнотачерез партиклы
            if(cupp>1000&&cupp<1500)
        //спасительный фект от других дебафов
                if(cupp>2000)
                //ефект темнота, фрозен эфект
                if(cupp>5000)
                    //голод получение урона + 2 эфекта
        return;
    }


}
