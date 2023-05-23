export class User {
    public id!: number;
    public userId!: string;
    public firstName!: string;
    public lastName!: string;
    public username!: string;
    public email!: string;
    public logInDateDisplay!: Date;
    public joinDate!: Date;
    public profileImageUrl!: string;
    public isActive!: boolean;
    public isNotLocked!: boolean;
    public role!: string;
    public authorities!: [];
    public profileImage!: File;
    public currentUsername!: string;

    constructor() {
        this.firstName = '';
        this.lastName = '';
        this.username = '';
        this.email = '';
        this.isActive = false;
        this.isNotLocked = false;
        this.role = '';
        this.authorities = [];
        this.currentUsername = '';
    }
}