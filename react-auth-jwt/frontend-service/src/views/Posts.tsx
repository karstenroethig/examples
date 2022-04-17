import { AxiosResponse } from 'axios';
import React from 'react';
import PostService from '../services/PostService';
import { PostType } from '../types/PostType';

interface Props {
}

interface State {
  posts: PostType[];
}

class Posts extends React.Component<Props, State> {
  constructor (props : Props) {
    super(props);
    this.state = {
      posts: []
    };
  }

  componentDidMount() {
    PostService.getPosts()
      .then((response: AxiosResponse<PostType[]>) => {
        this.setState({
          posts: response.data
        });
      })
      .catch((error: Error) => {
        console.log(error);
      });
  }

  render() {
    return (
      <>
        <h1>Posts</h1>
        <table className="table table-bordered table-striped table-hover">
          <thead className="table-light">
            <tr>
              <th>Username</th>
              <th>Title</th>
            </tr>
          </thead>
          <tbody>
            {this.state.posts.length < 1 &&
              <tr>
                <td colSpan={2} className="text-center">No posts available</td>
              </tr>
            }
            {this.state.posts.length > 0 &&
              <>
                {this.state.posts.map((post, postIndex) => (
                  <tr key={postIndex}>
                    <td>{post.username}</td>
                    <td>{post.title}</td>
                  </tr>
                ))}
              </>
            }
          </tbody>
        </table>
      </>
    );
  }
}

export default Posts;
