package com.example.adminscp;

class AdminDetails_class {
    // Static variable reference of single_instance
    // of type Singleton
    private static AdminDetails_class adminDetails_class = null;

    // Declaring a variable of type String
    public String email;
    public String pin;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private AdminDetails_class()
    {

    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    // Static method
    // Static method to create instance of Singleton class
    public static AdminDetails_class getInstance()
    {
        if (adminDetails_class == null)
            adminDetails_class = new AdminDetails_class();

        return adminDetails_class;
    }
}
