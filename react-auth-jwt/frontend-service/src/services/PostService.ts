import { AxiosResponse } from 'axios';
import { PostType } from '../types/PostType';
import API from './ApiCommon';

class PostService {
  getPosts = (): Promise<AxiosResponse<PostType[]>> => {
    return API.get<PostType[]>("/posts");
  };
}

export default new PostService();
