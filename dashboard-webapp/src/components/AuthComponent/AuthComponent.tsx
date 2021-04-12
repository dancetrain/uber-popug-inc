import React, { useState } from 'react';
import './AuthComponent.css';
import { Button } from "react-bootstrap";

const AuthComponent: React.FC = () => {
  const [register, setShowRegister] = useState(false)

  const toggleRegisterForm = () => {
    setShowRegister(!register)
  }

  const ToggleRegisterFormButton = <Button onClick={toggleRegisterForm}>
    {register ? "Login" : "Register"}
  </Button>

  if (register) {
    return (
      <div className="AuthComponent">
        Auth form Login
        {ToggleRegisterFormButton}
      </div>
    )
  } else {
    return (
    <div className="AuthComponent">
      Auth form Register
      {ToggleRegisterFormButton}
    </div>
  );
  }


}

export default AuthComponent;
