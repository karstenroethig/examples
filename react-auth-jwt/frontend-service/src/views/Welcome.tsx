import React from 'react';
import { Link } from 'react-router-dom';

class Welcome extends React.Component {
  render() {
    return (
      <div className="p-5 text-white bg-dark">
        <h1>Welcome to the React Auth JWT example</h1>
        <p>
          Your introduction could be here.
        </p>
        <hr className="my-4"></hr>
        <p className="lead">
          <Link to={"/posts"} className="btn btn-primary btn-lg">Show posts</Link>
        </p>
      </div>
    );
  }
}

export default Welcome;
