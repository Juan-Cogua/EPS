package model;

class Persona {
    private String name;
    private byte age;
    private String id;
    private String bloodType;
    private String address;
    private String phone;
    
    public Persona(String name, byte age, String id, String bloodType, String address, String phone) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.bloodType = bloodType;
        this.address = address;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public byte getAge() { return age; }
    public void setAge(byte age) { this.age = age; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // Métodos adicionales
    // 1. Mostrar información resumida
    public String resumen() {
        return "Nombre: " + name +
               ", ID: " + id +
               ", Edad: " + age +
               ", Tipo de sangre: " + bloodType +
               ", Direccion: " + address +
               ", Telefono: " + phone;
    }

    // 2. Validar si es mayor de edad
    public boolean esMayorDeEdad() {
        return age >= 18;
    }

    // 3. Actualizar información de contacto
    public void actualizarContacto(String nuevaDireccion, String nuevoTelefono) {
        this.address = nuevaDireccion;
        this.phone = nuevoTelefono;
    }

    // 4. Redefinir equals para comparar por ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) return false;
        Persona persona = (Persona) obj;
        return id.equals(persona.id); 
    }

    // 5. Redefinir hashCode para mantener consistencia con equals
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
