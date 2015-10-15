/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package broker.exceptions;

/**
 *
 * @author JoseJulio
 */
public class ServerErrorException extends Exception{

    private final String errorMessage = "An error has occurred on the server";

    @Override
    public String getMessage(){
        return errorMessage;
    }
    
}
