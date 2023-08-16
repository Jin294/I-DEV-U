import React, { useState, useEffect } from 'react';
import alert_css from '../sidebar/1alert.module.css';
import axios from 'axios';

import { useDispatch } from 'react-redux';
import { setChatIdx, setChatTitle, setModal, setSidebar } from '../../store/actions';

interface Props {
  backpage: () => void;
  Selalert: {
    idx: number;
    createdAt: string;
    type: string;
    fromUser:{
      idx:number,
      nickname:string,
      basicAnswerList:{
        surveyIdx:number,
        tag:string,
      }[]
      gender:string,
      intro:string|null,
      storedFileName:string|null,
    },
    toUser:{
      idx:number,
      nickname:string,
    },
    targetIdx:number|null,
    comment:string|null
  };
}


const DetailAlert: React.FC<Props> = ({ backpage, Selalert }) => {
  const dispatch = useDispatch()
  const [pjtdata, setpjtdata] = useState()

  useEffect(()=>{
    if (Selalert.type==='PROJECT') {
      const userToken = localStorage.getItem('userToken')
      axios({
        method:'get',
        url: `https://i9b206.p.ssafy.io:9090/project/detail/${Selalert.targetIdx}`,
        headers: {
          Authorization: 'Bearer ' + userToken
        },
      })
      .then(res=>
        setpjtdata(res.data.project.title))
      .catch(err=>console.log(err))
    }
  },[Selalert])

  const ok = () => {
    const userToken = localStorage.getItem('userToken')
    const now = new Date()
    // 채팅방 생성
    axios({
      method:'post',
      url: 'https://i9b206.p.ssafy.io:9090/chat/rooms',
      data:{
        userIdx:Selalert.fromUser.idx,
        title: Selalert.fromUser.nickname + '님의 mate 채팅방',
        createdAt:now
      },
      headers: {
        Authorization: 'Bearer ' + userToken
      },
    })
    .then(res => {
      const userIdxStr = localStorage.getItem('userIdx')
      const userIdx = userIdxStr ? parseInt(userIdxStr,10) : null

      axios({
        method:'delete',
        url: `https://i9b206.p.ssafy.io:9090/alarm/${Selalert.idx}`,
        headers: {
          Authorization: 'Bearer ' + userToken
        },
      })
      .then(res => console.log(res))
      .catch(err=>console.log(err))
      // 채팅방 참가
      dispatch(setChatIdx(res.data.data.roomIdx))
      dispatch(setChatTitle(Selalert.fromUser.nickname + '님의 mate 채팅방'))
      axios({
        method:'post',
        url: `https://i9b206.p.ssafy.io:9090/chat/rooms/${res.data.data.roomIdx}/users`,
        data:{
          userIdx:userIdx,
          updatedAt:now
        },
        headers: {
          Authorization: 'Bearer ' + userToken
        },
      })
      .then(() => {
        dispatch(setSidebar('채팅방'))
        dispatch(setModal(null))

      })
      .catch(err => console.log(err))
    })
    .catch(err => console.log(err))

  }

  const nope = () => {
    const userToken = localStorage.getItem('userToken')
     axios({
        method:'delete',
        url: `https://i9b206.p.ssafy.io:9090/alarm/${Selalert.idx}`,
        headers: {
          Authorization: 'Bearer ' + userToken
        },
      })
      .then(() => backpage())
      .catch(err=>console.log(err))
  }

  const meetingok = () => {
    const userToken = localStorage.getItem('userToken')
    // 수락시 알림 제거
    axios({
      method:'delete',
      url: `https://i9b206.p.ssafy.io:9090/alarm/${Selalert.idx}`,
      headers: {
        Authorization: 'Bearer ' + userToken
      },
    })
    .then(res => console.log(res,'삭제완료'))
    .catch(err=>console.log(err))
    // 프로젝트 참가 (백에서 비디오 룸까지 입장하게 처리됌)
    axios({
      method:'post',
      url: `https://i9b206.p.ssafy.io:9090/project/enter`,
      data:{
        roomIdx:Selalert.targetIdx,
        userIdx:Selalert.fromUser.idx,
      },
      headers: {
        Authorization: 'Bearer ' + userToken
      },
    })
    .then(() => {
      console.log('참가완료')
      dispatch(setModal(null))
    })
    .catch(err => console.log(err))
  }

  return (
    <div className={alert_css.alert_modal}>
      <div className={alert_css.buttons}>
        <p className={alert_css.backbtn} onClick={backpage}>돌아가기</p>
        <p className={alert_css.closebtn} onClick={() => { dispatch(setModal(null)) }}>닫기</p>
      </div>
      <br></br>
      <div className={alert_css.alert_detail}>
        {Selalert ? (
          <>
          <h1>알림 상세정보</h1>
          <div style={{display:'flex'}}>
            <div style={{width:'35%'}}>
              <img
                src={Selalert.fromUser.storedFileName ? Selalert.fromUser.storedFileName : "assets/default_profile.png"}
                alt=""
                style={{ borderRadius: "50%" , width:'125px', height:'125px' }}/>
                <p>
              닉네임 : {Selalert.fromUser.nickname}

                </p>
                <p>성별 : {Selalert.fromUser.gender}</p>
                <p>태그</p>
                <p>{Selalert.fromUser.basicAnswerList.map(a=>'#'+a.tag + ' ')}</p>
            </div>
            <div style={{margin:'auto'}}>
                {Selalert.type==='PROJECT' ? 
              // 화상신청 관련
                <div>
                  <h1>
                    {Selalert.type === 'PROJECT' ? <>
                    <p>프로젝트 : {pjtdata} </p>
                    <p>{Selalert.fromUser.nickname}님의 가입신청입니다.</p>
                    <p>{Selalert.comment}</p>
                    </>
                    : Selalert.type === 'MATE' ? `${Selalert.fromUser.nickname}님의 동료신청입니다.`
                    : Selalert.type === 'CHAT' ? `${Selalert.fromUser.nickname}님의 채팅신청입니다.`
                    : null}
                  </h1>

                  <button onClick={meetingok}>수락</button>
                  <button onClick={nope}>거절</button>
                </div>
              :
              // 채팅 신청에 관련
              <div>
                <button onClick={ok}>수락</button>
                <button onClick={nope}>거절</button>
              </div>}
            </div>
          </div>

          </>
        ) : (
          'Loading...'
        )}
      </div>
    </div>
  );
};

export default DetailAlert;