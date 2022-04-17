import React from 'react';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { Link } from 'react-router-dom';

interface Props {
}

interface State {
}

class NavigationBar extends React.Component<Props, State> {
  constructor(props: any) {
    super(props);
    this.state = {}
  }

  render() {
    return (
      <Navbar bg="dark" variant="dark">
        <Container>
          <Link to={"/"} className="navbar-brand">React Auth JWT</Link>
          <Nav className="me-auto">
            <Link to={"/"} className="nav-link">Home</Link>
            <Link to={"/posts"} className="nav-link">Posts</Link>
          </Nav>
          <Nav className="justify-content-end">
            <Link to={"/login"} className="nav-link">Login</Link>
          </Nav>
          <Nav className="justify-content-end">
            <span className="nav-link">[Username]</span>
            <a className="btn btn-danger" href="/logout">Logout</a>
          </Nav>
        </Container>
      </Navbar>
    );
  }
}

export default NavigationBar;
