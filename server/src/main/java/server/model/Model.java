package server.model;

import Exception.ServerErrorException;
import server.events.Event;
import server.misc.Observable;
import server.misc.Observer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by luisburgos on 2/09/15.
 */
public abstract class Model extends Observable {

    private HashMap<Integer, ArrayList<Observer>> setOfObservers;

    public Model(){
        setOfObservers = new HashMap<Integer, ArrayList<Observer>>();
    }

    public HashMap<Integer, ArrayList<Observer>> getSetOfObservers() {
        return setOfObservers;
    }

    @Override
    public void register(Event event, Observer observer) {
        getObserversList(event.getType()).add(observer);;
    }

    @Override
    public void unregister(Event event, Observer observer) {
        getObserversList(event.getType()).remove(observer);
    }

    @Override
    public void notify(Event event) {
        int eventType = event.getType();
        ArrayList observersToNotify = setOfObservers.get(eventType);
        Observer observer;
        for (Iterator it = observersToNotify.iterator(); it.hasNext();) {
            observer = (Observer)it.next();
            observer.update(event);
        }
    }

    private ArrayList<Observer> getObserversList(int eventType) {
        if (!setOfObservers.containsKey(eventType)) {
            setOfObservers.put(eventType, new ArrayList<Observer>());
        }
        return setOfObservers.get(eventType);
    }

    public void callFunctionByName(Class mClass, Class targetClass, String name, Object...params) throws ServerErrorException{
        Method method = null;
        try {
            method = mClass.getMethod(name, targetClass);
        } catch (SecurityException e) {
           throw new ServerErrorException();
        } catch (NoSuchMethodException e) {
            throw new ServerErrorException();
        }

        try {
            method.invoke(this, params);
        } catch (IllegalArgumentException e) {
            throw new ServerErrorException();
        } catch (IllegalAccessException e) {
            throw new ServerErrorException();
        } catch (InvocationTargetException e) {
            throw new ServerErrorException();
        }
    }

}
