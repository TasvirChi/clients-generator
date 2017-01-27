require 'rake'

spec = Gem::Specification.new do |s| 
  s.name = "borhan-client"
  s.version = "1.0"
  s.date = '2012-04-16'
  s.author = "Borhan Inc."
  s.email = "community@borhan.com"
  s.homepage = "http://www.borhan.com/"
  s.summary = "A gem implementation of Borhan's Ruby Client"
  s.description = "A gem implementation of Borhan's Ruby Client."
  s.files = FileList["lib/**/*.rb","Rakefile","README", "agpl.txt", "borhan.yml"].to_a
  s.test_files = FileList["{test}/test_helper.rb", "{test}/**/*test.rb", "{test}/media/*"].to_a
  s.add_dependency('rest-client')
end
