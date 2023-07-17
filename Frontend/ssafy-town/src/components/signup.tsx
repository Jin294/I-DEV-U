import React from 'react';
import { useFormik } from 'formik';
import { useNavigate } from 'react-router-dom';
import * as Yup from 'yup';
import './signup.css';
import axios from 'axios';

const validationSchema = Yup.object().shape({
  email: Yup.string()
    .email('유효하지 않은 이메일입니다.')
    .max(50, '이메일의 글자 수는 50자 이내로 설정해주세요.')
    .required('이메일을 입력해주세요.'),
  password: Yup.string()
    .min(8, '8~14 자리, 특수문자 사용불가')
    .max(14, '8~14 자리, 특수문자 사용불가')
    .required('비밀번호를 입력해주세요.'),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password') as any, null], '비밀번호가 일치하지 않습니다.')
    .required('비밀번호를 입력해주세요.'),
  nickname: Yup.string()
    .min(2, '2~12 자리, 특수문자 사용불가')
    .max(12, '2~12 자리, 특수문자 사용불가')
    .required('닉네임을 입력해주세요.'),
  name: Yup.string()
    .min(1, '1~12 자리, 특수문자 사용불가')
    .max(12, '1~12 자리, 특수문자 사용불가')
    .matches(/^[가-힣]+$/, '한글로만 작성해주세요.')
    .required('이름을 입력해주세요.'),
  birthday: Yup.string()
    .matches(/^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/, '유효하지 않은 생년월일입니다.')
    .required('생년월일을 입력해주세요.'),
  gender: Yup.number()
  .required('성별을 선택해주세요.')
  .oneOf([1, 2], '유효한 성별을 선택해주세요.'),
});

const SignupForm = () => {
  const navigate = useNavigate();
  const formik = useFormik({
    initialValues: {
      email: '',
      password: '',
      confirmPassword: '',
      nickname: '',
      name: '',
      birthday: '',
      gender: null,
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      // console.log(values);
      // 회원가입 요청 로직
      
      axios({
        method : 'post',
        url : 'http://localhost:8080/user/signup',
        data : values,
      })
      .then(res => {
        console.log(res)
        navigate('/login')
      })
      .catch(err => {
        console.log(err)
        alert('회원가입실패')
      })
    },
  });

  return (
    <div className="background">
      <div className="modal">
        <a className="link" href="/login">뒤로가기</a>
        <div className="logo"/>
        <form onSubmit={formik.handleSubmit}>
          <input className="input" type="text" placeholder="성함" {...formik.getFieldProps('name')} />
          {formik.touched.name && formik.errors.name ? <div>{formik.errors.name}</div> : null}

          <input className="input" type="date" {...formik.getFieldProps('birthday')} />
          {formik.touched.birthday && formik.errors.birthday ? <div>{formik.errors.birthday}</div> : null}

          <br />
          <label>성별</label>
            <div>
              <label>
                <input
                  type="radio"
                  name="gender"
                  value="1"
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  checked={formik.values.gender === '1'}
                />
                남성
              </label>
              <label>
                <input
                  type="radio"
                  name="gender"
                  value="2"
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  checked={formik.values.gender === '2'}
                />
                여성
              </label>
            </div>
          {formik.touched.gender && formik.errors.gender ? <div>{formik.errors.gender}</div> : null}

          <input className="input" type="text" placeholder="닉네임" {...formik.getFieldProps('nickname')} />
          {formik.touched.nickname && formik.errors.nickname ? <div>{formik.errors.nickname}</div> : null}

          <input className="input" type="text" placeholder="이메일" {...formik.getFieldProps('email')} />
          {formik.touched.email && formik.errors.email ? <div>{formik.errors.email}</div> : null}

          <input className="input" type="password" placeholder="비밀번호" {...formik.getFieldProps('password')} />
          {formik.touched.password && formik.errors.password ? <div>{formik.errors.password}</div> : null}

          <input className="input" type="password" placeholder="비밀번호 확인" {...formik.getFieldProps('confirmPassword')} />
          {formik.touched.confirmPassword && formik.errors.confirmPassword ? <div>{formik.errors.confirmPassword}</div> : null}

          <input type="file" name="photo" onChange={(event) => {
            formik.setFieldValue("photo", event?.currentTarget?.files?.[0]);
          }} />

          <button className="button" type="submit" disabled={!formik.isValid || formik.isSubmitting}>Sign Up</button>
        </form>
      </div>
    </div>
  );
};

export default SignupForm;
