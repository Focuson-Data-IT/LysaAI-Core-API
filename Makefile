.PHONY: deploy

deploy:
	@echo "Pulling latest Docker image..."
	ssh admin@103.30.195.110 "cd ~/LysaAI-Core-API && docker-compose pull"

	@echo "Restarting services..."
	ssh admin@103.30.195.110 "cd ~/LysaAI-Core-API && docker-compose up -d"

	@echo "Deployment complete!"