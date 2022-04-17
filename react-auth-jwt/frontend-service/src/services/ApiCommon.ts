import axios from 'axios';

export default axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-type': 'application/json',
    'Authorization': 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiTWlrZSIsImlhdCI6MTY1MjYyMzAyNSwiZXhwIjoxNjUyNjIzMjY1fQ.UNizJCVJaHDHrE_mJAw9ZdxWSi6UR6TeJQLxzHU7e50'
  }
});
