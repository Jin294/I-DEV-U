import { Client } from "@stomp/stompjs";


export interface AppState {
  isModalOpen: string|null;
  isSidebarOpen: string|null;
  isAllowMove:boolean;

  stompClientRef:Client|null;

  receivedAlert:string[];
  isChatIdx:number|null; //채팅방 넘어갈때 인덱스 저장해줄곳
  isChatTitle:string|null; //채팅방 넘어갈때 인덱스 저장해줄곳
  }
  