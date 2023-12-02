import { render, screen } from '@testing-library/react';
import App from './App';

test('Log In', () => {
  render(<App />);
  const loginButton = screen.getByText(/Log In/i);
  expect(loginButton).toBeInTheDocument;
});

test('User Name', () => {
  render(<App />);
  const userName = screen.queryByTestId('username');
  expect(userName).toBeInTheDocument;
});

test('Email', () => {
  render(<App />);
  const email = screen.queryByLabelText('Email');
  expect(email).not.toBeInTheDocument;
});
