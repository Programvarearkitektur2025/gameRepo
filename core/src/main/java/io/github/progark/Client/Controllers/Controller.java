package io.github.progark.Client.Controllers;
/*
 * Controller.java
 * This is an abstract class that defines the structure for all controllers in the application.
 * Each controller is responsible for managing a specific part of the application.
 * It provides methods for entering the controller, updating it with a delta time, and disposing of resources.
 */
public abstract class Controller {

    public abstract void enter();

    public abstract void update(float delta);

    public abstract void dispose();

}
