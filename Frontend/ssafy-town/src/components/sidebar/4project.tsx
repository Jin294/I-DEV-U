import React, { useState } from "react";
import project_css from "./4project.module.css";

import { useSelector, useDispatch } from 'react-redux';
import { setAllowMove, setModal } from '../../store/actions';
import { AppState } from '../../store/state';

import axios from "axios";
import ProjectFilter from "../filter/projectFilter";
import CreateProject from "../board/CreateProject";
import EnterProject from "../board/EnterProject";

const Project: React.FC = () => {
    const dispatch = useDispatch()
    const isModalOpen = useSelector((state: AppState) => state.isModalOpen);// 모달창 오픈여부 (알림, 로그아웃)
    const [projectList, setProjectList] = useState<string[]>(['프로젝트1','프로젝트2']);

  const loadproject = () => {
    axios({
      method:'get',
      url:'https://i9b206.p.ssafy.io:9090',
      // data
    })
    .then(res=>{
      console.log(res)
      setProjectList(res.data)
    })
    .catch(err=>{
      console.log(err)
    })
  }

  // input 방향키 살리기
  const handlekeydown = (event:React.KeyboardEvent<HTMLInputElement>) => {
    loadproject() //일단 박아둠 에러안뜨게
    const inputElement = event.currentTarget
    const currentCursorPosition = inputElement.selectionStart || 0;
    if (event.key === 'ArrowLeft' && currentCursorPosition!==0) {
      inputElement.setSelectionRange(currentCursorPosition - 1, currentCursorPosition - 1);
    } else if (event.key === 'ArrowRight') {
      inputElement.setSelectionRange(currentCursorPosition + 1, currentCursorPosition + 1);
    } else if (event.key === ' '){
      inputElement.value = inputElement.value.slice(0,currentCursorPosition)+ ' ' +inputElement.value.slice(currentCursorPosition,)
      inputElement.setSelectionRange(currentCursorPosition+1 , currentCursorPosition+1);
    }
  }

    return (
      <div className="sidebar_modal">
        <div style={{width:'100%', display:'flex',flexDirection:'column', alignItems:'center'}}>
          <h1>Project</h1>
          <div className={project_css.search}>
            <input type="text" placeholder='검색어를 입력해주세요' onKeyDown={handlekeydown}
            onFocus={()=>dispatch(setAllowMove(false))} onBlur={()=>dispatch(setAllowMove(true))}/>
            <button>검색</button>
          </div>
          <div style={{display:'flex', width:'85%'}}>
            <button className={project_css.button} onClick={()=>dispatch(setModal('프로젝트필터'))}>필터</button>
            <button className={project_css.button} onClick={()=>dispatch(setModal('프로젝트생성'))}>방 생성</button>
          </div>
          <hr style={{width:'75%', color:'black'}}/>
          <div className={project_css.scrollbox}>
            {projectList.map((project) => (
              <div>
                <div className={project_css.project} key={project}>
                  <img src="assets/default_profile.png" alt=""/>
                  <div className={project_css.project_data}>
                    <b>{project}</b>
                    <p style={{color:'gray'}}>#Python #Java #JavaScript #React </p>
                  </div>
                  <div>
                    <button className={project_css.btn} onClick={()=>{dispatch(setModal('참가신청'))}}>참가신청</button>
                    <span>2/5</span>
                  </div>
                </div>
                <hr />
              </div>
            ))} 
          </div>
        </div>
        {isModalOpen==='프로젝트필터' ? <ProjectFilter />:
        isModalOpen==='프로젝트생성' ? <CreateProject /> :
        isModalOpen==='참가신청' ? <EnterProject /> :
        null}
      </div>
    );
  };

  export default Project;
