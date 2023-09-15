const UserProfile = ({name, age, gender, imageNumb, ...props}) => {

    gender = gender === "MALE" ? "men" : "women";

    return (
        <div>
            <h1>{name}</h1>
            <p>{age}</p>
            <img src={`https://randomuser.me/api/portraits/${gender}/${imageNumb}.jpg`}/>
            {props.children}
        </div>
    )
}

export default UserProfile;

