import React from 'react';
import editsel_css from './editsel.module.css';

import { useDispatch } from 'react-redux';
import { setModal } from '../../store/actions';

const EditSel: React.FC = () => {

  const dispatch = useDispatch();

  function onClose() {
    dispatch(setModal(null));
  }
  return (
    <div className={editsel_css.modal_overlay}>
      <div className={editsel_css.withdraw_modal}>
        <div className={editsel_css.two_btn}>
        <span></span>
          <span
            onClick={() => {
              onClose();
            }}>
            닫기
          </span>
        </div>
        <h1 style={{ marginBottom: '20px' }}>
          회원정보 수정
        </h1>
        <div className={editsel_css.btn}>
          <button onClick={()=>{dispatch(setModal('회원정보수정3'))}}>내 정보 수정</button>
          <button onClick={()=>{dispatch(setModal('비밀번호변경'))}}>비밀번호 변경</button>
        </div>
        <p className={editsel_css.withdraw} onClick={()=>dispatch(setModal('회원탈퇴'))}>회원탈퇴</p>
      </div>
    </div>
  );
};

export default EditSel;