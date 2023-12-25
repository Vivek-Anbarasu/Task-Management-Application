
import toast from 'react-hot-toast';
import * as Yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import axios from 'axios';

export const Register = (props) => {

    const registerApplication = async (data) => {
      try {
        const response = await axios.post(process.env.REACT_APP_Reg_URL, {name: data.username,password: data.password,
          email: data.email,roles: 'ROLE_ADMIN'});
          if(response.data === 'User Succesfully Registered'){
            toast.success(response.data); 
            props.onFormSwitch('login');
        }else{
            toast.error(response.data);
        }
      } catch (error) {
        console.log(error);
        toast.error("Registeration Failed");
      }
    };

    const schema = Yup.object().shape({
        username: Yup.string().required("Please enter your username").min(3, "Must Contain 3 Characters").max(100, "Cannot exceed 100 characters"),
        password: Yup.string().required("Please enter your password").min(8, "Must Contain 8 Characters").max(100, "Cannot exceed 100 characters")
        .matches(
          /^(?=.*[a-z])/,
          " Must Contain One Lowercase Character"
        )
        .matches(
          /^(?=.*[A-Z])/,
          "  Must Contain One Uppercase Character"
        )
        .matches(
          /^(?=.*[0-9])/,
          "  Must Contain One Number Character"
        )
        .matches(
          /^(?=.*[!@#$%^&*])/,
          "  Must Contain  One Special Case Character"
        ),
        email: Yup.string().required("Please enter your email")
      });

      const { register, handleSubmit, formState: {errors} } = useForm({
        mode: 'onSubmit',
        resolver: yupResolver(schema)
      });

    return (
        <div className="auth-form-container">
            <h2>Register</h2>
        <form className="register-form" onSubmit={handleSubmit(registerApplication)}>
                <label htmlFor="username">User Name</label>
                <div className="error-message">{errors.username && <p>{errors.username.message}</p>}</div>
                <input type="username" placeholder="user name" id="username" name="username" {...register('username')}  autoComplete="off"/>
          
                <label htmlFor="password">Password</label>
                <div className="error-message">{errors.password && <p>{errors.password.message}</p>}</div>
                <input  type="password" placeholder="********" id="password" name="password" {...register('password')} />

                <label htmlFor="email">Email</label>
                <div className="error-message">{errors.email && <p>{errors.email.message}</p>}</div>
                <input  type="email" placeholder="email" id="email" name="email" {...register('email')} autoComplete="off"/>

            <button type="submit">Register</button>
        </form>
        <button className="link-btn" onClick={() => props.onFormSwitch('login')}>Already have an account? Login here.</button>
    </div>
    )
}
