-- Sample team members data for the "Meet the Team" section
INSERT INTO team_members (name, role, bio, image_url, display_order) VALUES
('John Smith', 'Lead Developer', 'Passionate full-stack developer with 5+ years of experience in Spring Boot and React. Loves building scalable social platforms.', 'https://via.placeholder.com/300x300?text=John', 1),
('Sarah Johnson', 'UI/UX Designer', 'Creative designer focused on user experience and modern web design. Specialized in creating intuitive social media interfaces.', 'https://via.placeholder.com/300x300?text=Sarah', 2),
('Mike Chen', 'Backend Engineer', 'Expert in microservices architecture and database optimization. Ensures robust and scalable backend systems.', 'https://via.placeholder.com/300x300?text=Mike', 3),
('Emily Davis', 'Product Manager', 'Strategic thinker who bridges the gap between technical implementation and user needs. Focus on social platform growth.', 'https://via.placeholder.com/300x300?text=Emily', 4)
ON CONFLICT DO NOTHING;