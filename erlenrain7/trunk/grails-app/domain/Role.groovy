class Role {

    String roleName
    String description

    static belongsTo = User

    static optionals = ["description"]

}
