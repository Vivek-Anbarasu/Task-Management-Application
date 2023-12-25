import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import {App} from '../components/App';

describe('App Component', () => {

  it('renders the default login form', () => {
    render(<App/>);
    expect(screen.getByText('Login')).toBeInTheDocument();
  });

  it('renders the registration form when switched', () => {
    render(<App/>);
    fireEvent.click(screen.getByText("Don't have an account? Register here."));
    const title = screen.getAllByText('Register');
    expect(title[0]).toBeInTheDocument();
  });
}); 