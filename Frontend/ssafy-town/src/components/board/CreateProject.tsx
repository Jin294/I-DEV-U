import React, { useState } from 'react';
import Create_css from './CreateProject.module.css';

import { useDispatch } from 'react-redux';
import { setModal } from '../../store/actions';
import axiosInstance from '../../interceptors'; // axios 인스턴스 가져오기

const CreateProject: React.FC = () => {

  const OPENVIDU_SERVER_URL = process.env.REACT_APP_OPENVIDU_SERVER_URL;
  const OPENVIDU_SECRET = process.env.REACT_APP_OPENVIDU_SECRET;
  const BACKEND_SERVER_URL = process.env.REACT_APP_BACKEND_SERVER_URL;
// const BACKEND_SERVER_URL = 'https://i9b206.p.ssafy.io:9090';
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [total_num, setTotalNum] = useState(2);
  const languages = ["Python", "Java", "C", "C++", "C#", "Object-C", "Kotlin", "Swift", "Ruby", "Go", "Javascript", "typescript", "PyPy", "PHP", "Rust", "Text", "D", "기타"];
  const [selectedLanguages, setSelectedLanguages] = useState<{language:string;}[]>([]);
  const [projectType, setProjectType] = useState('PROJECT');

  const handleLanguageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (e.target.checked) {
      if (selectedLanguages.length < 5) {
        setSelectedLanguages(prev => [...prev, {language:value}]);
      } else {
        e.preventDefault();
        alert('최대 5개의 언어만 선택할 수 있습니다.');
      }
    } else {
      setSelectedLanguages(prev => prev.filter(lang => lang.language !== value));
    }
  }

  const dispatch=useDispatch()

  const handlekeydown = (event:React.KeyboardEvent<HTMLTextAreaElement>|React.KeyboardEvent<HTMLInputElement>) => {
    const inputElement = event.currentTarget
    const currentCursorPosition = inputElement.selectionStart || 0;
    const maxCursorPosition = inputElement.value.length;
    if (event.key === 'ArrowLeft' && currentCursorPosition!==0) {
      inputElement.setSelectionRange(currentCursorPosition - 1, currentCursorPosition - 1);
    } else if (event.key === 'ArrowRight') {
      inputElement.setSelectionRange(currentCursorPosition + 1, currentCursorPosition + 1);
    } else if (event.key === ' '){
      inputElement.value = inputElement.value.slice(0,currentCursorPosition)+ ' ' +inputElement.value.slice(currentCursorPosition,)
      inputElement.setSelectionRange(currentCursorPosition+1 , currentCursorPosition+1);
    } else if (event.key ==='ArrowUp' && currentCursorPosition >= 29) {
      inputElement.setSelectionRange(currentCursorPosition-29 , currentCursorPosition-29);
    } else if (event.key ==='ArrowDown' && currentCursorPosition+29 < maxCursorPosition) {
      inputElement.setSelectionRange(currentCursorPosition+29 , currentCursorPosition+29);
    }
  }

  const Create = () => {
    if (title.length < 6) {
      alert('프로젝트명은 최소 6자 이상이어야 합니다.');
      return;
    }

    if (selectedLanguages.length<1) {
      alert('프로젝트 언어를 1개 이상 선택해주세요')
      return
    }

    // OpenVidu 세션 생성
    const userToken = localStorage.getItem('userToken')
    axiosInstance.post(OPENVIDU_SERVER_URL +'/api/sessions', {}, {
        headers: {
            'Authorization': 'Basic ' + btoa('OPENVIDUAPP:' + OPENVIDU_SECRET)
        }
    })
    .then((response) => {
        const sessionId = response.data.id;
        localStorage.setItem("OVsession",sessionId);

        const userIdxStr = localStorage.getItem('userIdx')
        var userIdx:number|null;
        if (userIdxStr) {userIdx=parseInt(userIdxStr,10)} else {userIdx=null}

        // 화상방 생성
        axiosInstance({
          method:'post',
          url:BACKEND_SERVER_URL+'/video/create',
          data:{
            title:title,
            userIdx:userIdx,
            ovSession:sessionId
          },
          headers: {
            Authorization: 'Bearer ' + userToken
          },
        })
        .then(res=>{
          console.log(res)
          dispatch(setModal(null)); // 모달닫기
        })
        .catch(err=>console.log(err))

        // 백엔드에 프로젝트 정보, 세션 ID 전송
        axiosInstance({
          method:'post',
          url:BACKEND_SERVER_URL+'/project/register',
          data:{
            userIdx: userIdx,
            title: title, //6~30자
            content: content, // ~1000자
            totalNum: total_num, // 2~6
            nowNum: 1,
            type: projectType, // PROJECT or STUDY로 보내짐,
            session: sessionId,
            languageList:selectedLanguages, // 배열 최대 5개
          },
          headers: {
            Authorization: 'Bearer ' + userToken
          },
        })
        .then((res) => {
          console.log(res);
          alert("프로젝트가 생성되었습니다.")
        })
        .catch((err) => {
          console.log(err);
          alert("프로젝트 생성 실패.")
        });
      })
      .catch((err) => {
        console.log(err);
      });
    };

    return (
      <div className={Create_css.modal_overlay} onMouseDown={(e: React.MouseEvent<HTMLDivElement>) => {
        if (e.target === e.currentTarget) {
          dispatch(setModal(null))
        }
      }}>
        <div className={Create_css.modal}>
          <p className={Create_css.closebtn} onClick={() => { dispatch(setModal(null)) }}>닫기</p>
          <h1>새 프로젝트 생성</h1>
          <hr />
          <div className={Create_css.info}>
    
            <div className={Create_css.input}>
              <label className={Create_css.label}><span>프</span><span>로</span><span>젝</span><span>트</span><span>명</span></label>
              <p> : </p>
              <input
                className={Create_css.input1}
                type="text"
                id="title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                onKeyDown={handlekeydown}
                maxLength={30}
                placeholder='6~30자 이내 작성'
              />
            </div>
    
            <div className={Create_css.input}>
              <label className={Create_css.label}><span>인</span><span>원</span><span>제</span><span>한</span></label>
              <p> : </p>
              <input
                className={Create_css.input1}
                type="number"
                id="total_num"
                value={total_num}
                onChange={(e) => setTotalNum(parseInt(e.target.value,10))}
                onKeyDown={(e) => e.preventDefault()}
                min="2"
                max="6"
                step="1"
                defaultValue={4}
                style={{ width: '10%' }}
              />
              <span style={{color : 'red', marginLeft: '5px'}}>최대 6명</span>
            </div>
    
            <div className={Create_css.input}>
              <label className={Create_css.label}><span>소</span><span>개</span></label>
              <p> : </p>
              <textarea
                name=""
                id="content"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                onKeyDown={handlekeydown}
                maxLength={1000}
              ></textarea>
            </div>
            <div className={Create_css.input}>
              <label className={Create_css.label}><span>타</span><span>입</span></label>
              <p> : </p>
              <span>
                <input
                  type="radio"
                  value="PROJECT"
                  checked={projectType === 'PROJECT'}
                  onChange={(e) => setProjectType(e.target.value)}
                /> 프로젝트 </span>
              <span>
                <input
                  type="radio"
                  value="STUDY"
                  checked={projectType === 'STUDY'}
                  onChange={(e) => setProjectType(e.target.value)}
                /> 스터디 </span>
            </div>
    
            <div className={Create_css.input}>
              <label className={Create_css.label}><span>언</span><span>어</span></label>
              <p> : </p>
              <div className={Create_css.choose} style={{width:'60%', whiteSpace:'pre-wrap'}}>
              {languages.map((lang, index) => (
                <label key={index} style={{whiteSpace:'pre-wrap'}}>
                  <input
                    type="checkbox"
                    value={lang}
                    onChange={handleLanguageChange}
                    checked={selectedLanguages.some(selLang => selLang.language === lang)}
                  />
                  <span> {lang} </span> 
                </label>
              ))}
              </div>
            </div>

            <button className={Create_css.button} onClick={() => Create()}>생성하기</button>
          </div>
        </div>
      </div>
    );
  };    

export default CreateProject;
