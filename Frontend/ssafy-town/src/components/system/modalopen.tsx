import React from 'react';

// 모달 리스트
import Alert from '../sidebar/1alert'
import AllNotice from '../notice/all_notice';
import AllAlert from '../notice/all_alert';
import Logout from '../account/logout'
import FirstQA from '../survey/firstQA';
import ReFirstQA from '../survey/RefirstQA';
import QnA from '../board/QnA';

import CreateProject from '../board/CreateProject';
import EnterProject from '../board/EnterProject';
import SogaeFilter from '../filter/sogaeFilter';

import { useSelector } from 'react-redux';
import { AppState } from '../../store/state';
import DetailProject from '../board/DetailProject';
import AlertResponse from '../board/AlertResponse';
import CheckPass from '../account/checkpass';
import EditSel from '../account/editsel';
import Withdraw from '../account/withdraw';

const ModalOpen: React.FC = () => {
  const isModalOpen = useSelector((state: AppState) => state.isModalOpen);//사이드바 오픈여부
  return (
    <>
      { isModalOpen === '최초설문' ? <FirstQA />
      : isModalOpen === 'Re최초설문' ? <ReFirstQA />
      : isModalOpen === '공지알림' ? <Alert />
      : isModalOpen === '공지전체' ? <AllNotice />
      : isModalOpen === '알림전체' ? <AllAlert />
      : isModalOpen === '로그아웃' ? <Logout />
      : isModalOpen === 'QnA게시판' ? <QnA />
      : isModalOpen === '프로젝트생성' ? <CreateProject />
      : isModalOpen === '프로젝트참가신청' ? <EnterProject />
      : isModalOpen === '프로젝트상세정보' ? <DetailProject />
      : isModalOpen === '소개팅필터' ? <SogaeFilter />
      : isModalOpen === '프로젝트가입알림' ? <AlertResponse />
      : isModalOpen === '회원정보수정1' ? <CheckPass />
      : isModalOpen === '회원정보수정2' ? <EditSel />
      : isModalOpen === '회원탈퇴' ? <Withdraw />
      : null }
    </>
  );
}

export default ModalOpen;
