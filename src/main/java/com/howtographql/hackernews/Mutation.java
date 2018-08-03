package com.howtographql.hackernews;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;

public class Mutation implements GraphQLRootResolver {

	private final LinkRepository linkRepository;
	private final UserRepository userRepository;

	public Mutation(LinkRepository linkRepository, UserRepository userRepository) {
		this.linkRepository = linkRepository;
		this.userRepository = userRepository;
	}

	public Link createLink(String url, String description, DataFetchingEnvironment env) {
		System.out.print("AAAAAAA");
		AuthContext context = env.getContext();
		System.out.print("Contex:" + context);
		Link newLink = new Link(url, description, context.getUser().getId());
		System.out.print("BBBBBBB");
		linkRepository.saveLink(newLink);
		System.out.print("CCCCCCC");
		return newLink;
	}

	public User createUser(String name, AuthData auth) {
		User newUser = new User(name, auth.getEmail(), auth.getPassword());
		return userRepository.saveUser(newUser);
	}

	public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
		User user = userRepository.findByEmail(auth.getEmail());
		if (user.getPassword().equals(auth.getPassword())) {
			return new SigninPayload(user.getId(), user);
		}
		throw new GraphQLException("Invalid credentials");
	}

}
