/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.Scanner;

/**
 *
 * @author erick
 */
public class SingleResponsability {

    public static void main(String[] args) {
       int opcion=0;
       Scanner sc=new Scanner(System.in);
       Coche coche=new Coche();
       CocheDB cocheDB=new CocheDB();
       String marca;
       while (opcion!=3){
           System.out.println("1.- Crear coche");
           System.out.println("2.- Guardar en la Base de Datos");
           System.out.println("3.- Salir");
           System.out.println("Elija opcion");
           opcion=sc.nextInt();
           switch (opcion) {
               case 1:
                   System.out.println("Que marca es el coche");
                   marca=sc.next();
                   coche=new Coche (marca);
                   break;
               case 2:   
                   cocheDB.guardarCocheDb(coche);
                   break;
           }
           
       }
           
    }
}