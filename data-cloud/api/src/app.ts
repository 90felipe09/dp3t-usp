import { initServer } from "./index";


const retryInitServer = () => {
  try{
    initServer();
  }
  catch{
    retryInitServer();
  }
}

retryInitServer();